package buckpal.account.application.service;

import buckpal.account.domain.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class MoneyTransferProperties {

    private Money maximumTransferThreshold = Money.of(1_000_000L);
}
