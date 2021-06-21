package buckpal.account.application.port.in;

import buckpal.account.domain.Account.AccountId;
import buckpal.account.domain.Money;

public interface GetAccountBalanceQuery {

    Money getAccountBalance(AccountId accountId);
}
