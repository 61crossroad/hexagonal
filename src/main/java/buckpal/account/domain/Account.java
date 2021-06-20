package buckpal.account.domain;

import lombok.Value;

public class Account {

    private AccountId id;
    private Money baselineBalance;
    private ActivityWindow activityWindow;

    public Money calculateBalance() {
        return Money.add(
                this.baselineBalance,
                this.activityWindow.calculateBalance(this.id));
    }



    @Value
    public static class AccountId {
        private Long value;
    }
}
