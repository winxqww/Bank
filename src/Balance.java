import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Balance {

    private final long amount;
    private final Currency currency;

    public Balance(long amount, Currency currency) {

        if (amount < 0) {
            throw new IllegalArgumentException(
                    "Баланс не может быть отрицательным"
            );
        }

        this.currency = Objects.requireNonNull(
                currency,
                "Валюта не может быть null"
        );

        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Balance add(Balance other) {

        Objects.requireNonNull(
                other,
                "Баланс для сложения не может быть null"
        );

        if (this.currency != other.currency) {
            throw new IllegalArgumentException(
                    "Нельзя складывать разные валюты"
            );
        }

        long newAmount = Math.addExact(
                this.amount,
                other.amount
        );

        return new Balance(
                newAmount,
                this.currency
        );
    }

    public Balance subtract(Balance other) {

        Objects.requireNonNull(
                other,
                "Баланс для вычитания не может быть null"
        );

        if (this.currency != other.currency) {
            throw new IllegalArgumentException(
                    "Нельзя вычитать разные валюты"
            );
        }

        if (other.amount > this.amount) {
            throw new IllegalArgumentException(
                    "Недостаточно средств"
            );
        }

        long newAmount = this.amount - other.amount;

        return new Balance(
                newAmount,
                this.currency
        );
    }

    public Balance convertTo(Currency targetCurrency) {

        Objects.requireNonNull(
                targetCurrency,
                "Целевая валюта не может быть null"
        );

        if (this.currency == targetCurrency) {
            return new Balance(
                    this.amount,
                    this.currency
            );
        }

        BigDecimal targetAmount =
                BigDecimal.valueOf(this.amount)
                        .multiply(
                                BigDecimal.valueOf(
                                        this.currency
                                                .getPriceInRublesKopecks()
                                )
                        )
                        .divide(
                                BigDecimal.valueOf(
                                        targetCurrency
                                                .getPriceInRublesKopecks()
                                ),
                                0,
                                RoundingMode.HALF_UP
                        );

        return new Balance(
                targetAmount.longValueExact(),
                targetCurrency
        );
    }

    @Override
    public String toString() {

        BigDecimal majorUnits =
                BigDecimal.valueOf(amount)
                        .divide(
                                BigDecimal.valueOf(100),
                                2,
                                RoundingMode.UNNECESSARY
                        );

        return majorUnits.toPlainString()
                + " "
                + currency.getSymbol();
    }
}