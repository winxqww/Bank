public class Main {

    public static void main(String[] args) {

        BankAccount account1 = new BankAccount(
                "1001",
                "Анна",
                new Balance(100_000, Currency.RUB),
                new Balance(50_000, Currency.RUB)
        );

        BankAccount account2 = new BankAccount(
                "1002",
                "Иван",
                new Balance(50_000, Currency.USD),
                new Balance(10_000, Currency.USD)
        );

        System.out.println("=== Начальные счета ===");

        System.out.println(account1);
        System.out.println(account2);

        System.out.println("\n=== Пополнение ===");

        account1.deposit(
                new Balance(20_000, Currency.RUB)
        );

        System.out.println(account1);

        System.out.println("\n=== Снятие ===");

        account1.withdraw(
                new Balance(10_000, Currency.RUB)
        );

        System.out.println(account1);

        System.out.println("\n=== Перевод ===");

        account1.transferTo(
                account2,
                new Balance(5_000, Currency.RUB)
        );

        System.out.println(account1);
        System.out.println(account2);

        System.out.println("\n=== Конвертация ===");

        Balance rubles =
                new Balance(10_000, Currency.RUB);

        Balance dollars =
                rubles.convertTo(Currency.USD);

        Balance euros =
                rubles.convertTo(Currency.EUR);

        Balance yuan =
                rubles.convertTo(Currency.CNY);

        System.out.println("Рубли: " + rubles);
        System.out.println("Доллары: " + dollars);
        System.out.println("Евро: " + euros);
        System.out.println("Юани: " + yuan);

        System.out.println("\n=== Блокировка ===");

        account1.blockAccount();

        System.out.println(account1);

        try {
            account1.deposit(
                    new Balance(1_000, Currency.RUB)
            );
        } catch (IllegalStateException e) {
            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }

        System.out.println("\n=== Разблокировка ===");

        account1.unblockAccount();

        account1.deposit(
                new Balance(1_000, Currency.RUB)
        );

        System.out.println(account1);
    }
}