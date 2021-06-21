package buckpal.account.application.service;

import buckpal.account.application.port.in.SendMoneyCommand;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
public class SendMoneyService {

    /*
    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;
     */

    // @Override
    public boolean sendMoney(SendMoneyCommand command) {
        // Validate here!
        return true;
    }
}
