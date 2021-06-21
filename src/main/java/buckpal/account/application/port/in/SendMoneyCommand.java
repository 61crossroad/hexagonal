package buckpal.account.application.port.in;

import buckpal.account.domain.Account.AccountId;
import buckpal.account.domain.Money;
import buckpal.common.SelfValidating;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {

    @NotNull
    private final AccountId sourceAccountId;

    @NotNull
    private final AccountId targetAccountId;

    @NotNull
    private final Money money;

    public SendMoneyCommand(AccountId sourceAccountId, AccountId targetAccountId, Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;

        if (!money.isPositive()) {
            throw new IllegalArgumentException("Amount of money must be greater than 0");
        }
        this.validateSelf();
    }
}
