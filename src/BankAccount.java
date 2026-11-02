import java.util.Objects;

public class BankAccount {

    private final String accountNumber;
    private final String owner;
    private final Balance withdrawalLimit;

    private Balance balance;
    private boolean blocked;

    public BankAccount(
            String accountNumber,
            String owner,
            Balance balance,
            Balance withdrawalLimit
    ) {

        if (accountNumber == null
                || accountNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Номер счёта не может быть пустым"
            );
        }

        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException(
                    "Имя владельца не может быть пустым"
            );
        }

        this.accountNumber = accountNumber;
        this.owner = owner;

        this.balance = Objects.requireNonNull(
                balance,
                "Баланс не может быть null"
        );

        this.withdrawalLimit = Objects.requireNonNull(
                withdrawalLimit,
                "Лимит снятия не может быть null"
        );

        if (balance.getCurrency()
                != withdrawalLimit.getCurrency()) {
            throw new IllegalArgumentException(
                    "Баланс и лимит должны иметь одинаковую валюту"
            );
        }

        this.blocked = false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public Balance getBalance() {
        return balance;
    }

    public Balance getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void deposit(Balance amount) {

        checkAccount();

        Objects.requireNonNull(
                amount,
                "Сумма пополнения не может быть null"
        );

        checkCurrency(amount);

        if (amount.getAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Сумма пополнения должна быть положительной"
            );
        }

        balance = balance.add(amount);
    }

    public void withdraw(Balance amount) {

        checkAccount();

        Objects.requireNonNull(
                amount,
                "Сумма снятия не может быть null"
        );

        checkCurrency(amount);

        if (amount.getAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Сумма снятия должна быть положительной"
            );
        }

        if (amount.getAmount()
                > withdrawalLimit.getAmount()) {
            throw new IllegalArgumentException(
                    "Превышен лимит снятия"
            );
        }

        balance = balance.subtract(amount);
    }

    public void transferTo(
            BankAccount targetAccount,
            Balance amount
    ) {

        checkAccount();

        Objects.requireNonNull(
                targetAccount,
                "Получатель не может быть null"
        );

        Objects.requireNonNull(
                amount,
                "Сумма перевода не может быть null"
        );

        targetAccount.checkAccount();

        if (this == targetAccount) {
            throw new IllegalArgumentException(
                    "Нельзя переводить деньги самому себе"
            );
        }

        if (amount.getAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Сумма перевода должна быть положительной"
            );
        }

        checkCurrency(amount);

        if (amount.getAmount()
                > withdrawalLimit.getAmount()) {
            throw new IllegalArgumentException(
                    "Превышен лимит перевода"
            );
        }

        Balance convertedAmount =
                amount.convertTo(
                        targetAccount.balance.getCurrency()
                );

        Balance newSourceBalance =
                this.balance.subtract(amount);

        Balance newTargetBalance =targetAccount.balance.add(convertedAmount);

        this.balance = newSourceBalance;

        targetAccount.balance = newTargetBalance;
    }

    public void blockAccount() {
        blocked = true;
    }

    public void unblockAccount() {
        blocked = false;
    }

    private void checkAccount() {

        if (blocked) {
            throw new IllegalStateException(
                    "Счёт заблокирован"
            );
        }
    }

    private void checkCurrency(Balance amount) {

        if (this.balance.getCurrency()
                != amount.getCurrency()) {
            throw new IllegalArgumentException(
                    "Валюта операции не совпадает с валютой счёта"
            );
        }
    }

    @Override

    public String toString() {

        return "BankAccount{"
                + "accountNumber='" + accountNumber + '\''
                + ", owner='" + owner + '\''
                + ", balance=" + balance
                + ", withdrawalLimit=" + withdrawalLimit
                + ", blocked=" + blocked
                + '}';
    }
}