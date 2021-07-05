package buckpal.account.application.service;

import buckpal.account.application.port.in.SendMoneyCommand;
import buckpal.account.application.port.in.SendMoneyUseCase;
import buckpal.account.application.port.out.AccountLock;
import buckpal.account.application.port.out.LoadAccountPort;
import buckpal.account.application.port.out.UpdateAccountStatePort;
import buckpal.account.domain.Account;
import buckpal.account.domain.Account.AccountId;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
class SendMoneyService implements SendMoneyUseCase {

    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;
    private final MoneyTransferProperties moneyTransferProperties;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {

        // Validate here!
        checkThreshold(command);

        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

        Account sourceAccount = loadAccountPort.loadAccount(
                command.getSourceAccountId(),
                baselineDate
        );

        Account targetAccount = loadAccountPort.loadAccount(
                command.getTargetAccountId(),
                baselineDate
        );

        AccountId sourceAccountId = sourceAccount.getId()
                .orElseThrow(() -> new IllegalStateException("expected source account ID not to be empty"));
        AccountId targetAccountId = targetAccount.getId()
                .orElseThrow(() -> new IllegalStateException("expected target account Id not to be empty"));
        // End validate!

        accountLock.lockAccount(sourceAccountId);
        // withdraw and addActivity
        if (!sourceAccount.withdraw(command.getMoney(), targetAccountId)) {
            accountLock.releaseAccount(sourceAccountId);
            return false;
        }

        accountLock.lockAccount(targetAccountId);
        // deposit and addActivity
        if (!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
            accountLock.releaseAccount(sourceAccountId);
            accountLock.releaseAccount(targetAccountId);
            return false;
        }

        // activities are already added, Account did it
        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);

        accountLock.releaseAccount(sourceAccountId);
        accountLock.releaseAccount(targetAccountId);
        return true;
    }

    private void checkThreshold(SendMoneyCommand command) {
        if (command.getMoney().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())) {
            throw new ThresholdExceededException(moneyTransferProperties.getMaximumTransferThreshold(), command.getMoney());
        }
    }
}
