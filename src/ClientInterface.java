import exceptions.AccountException;
import exceptions.AccountNotFoundException;
import exceptions.DuplicateUserIdException;
import exceptions.InvalidAmountException;
import exceptions.InvalidUserIdException;
import exceptions.NonZeroBalanceException;
import exceptions.PasswordMismatchException;
import exceptions.WrongPasswordException;
import model.BankAccount;
import model.CheckingAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientInterface {

    private final List<BankAccount> accounts = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    private long nextAccountNumber = 1;

    private final List<String> options = List.of(
            "1 - Створити рахунок",
            "2 - Видалити рахунок",
            "3 - Внести кошти на рахунок",
            "4 - Зняти кошти з рахунку",
            "5 - Інформація щодо рахунку",
            "6 - Вихід"
    );

    public void start() {
        while (true) {
            printMainMenu();

            String option;
            try {
                option = scanner.nextLine().trim();
            } catch (NoSuchElementException e) {
                return;
            }

            try {
                switch (option) {
                    case "1" -> addNewAccount();
                    case "2" -> deleteAccount();
                    case "3" -> deposit();
                    case "4" -> withdraw();
                    case "5" -> printInfo();
                    case "6" -> {
                        System.out.println("До побачення!");
                        return;
                    }
                    default -> System.err.println("Помилка: оберіть пункт меню від 1 до 6");
                }
            } catch (AccountException e) {
                System.err.println("Помилка: " + e.getMessage());
            } catch (NoSuchElementException e) {
                return;
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n****************");
        System.out.println("Банківська система: ");
        System.out.println("****************");
        options.forEach(System.out::println);
        System.out.print("\n>>> Оберіть пункт меню: ");
    }

    private void addNewAccount() {
        printHeader("Створення нового рахунку: ");

        String name = readLine(">>> Введіть ім'я клієнта: ");
        Long userId = readUserId();
        if (findAccount(userId) != null) {
            throw new DuplicateUserIdException("Рахунок з userId " + userId + " вже існує");
        }

        String password = readLine(">>> Введіть пароль: ");
        BankAccount.validatePassword(password);
        String confirmPassword = readLine(">>> Підтвердіть пароль: ");
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException("Пароль та підтвердження паролю не збігаються");
        }

        BankAccount account = new CheckingAccount(name, nextAccountNumber, userId, password);
        accounts.add(account);
        nextAccountNumber++;

        System.out.println("\nРахунок №" + account.getAccountNumber() + " для клієнта "
                + account.getCustomerName() + " успішно створено");
    }

    private void deleteAccount() {
        printHeader("Видалення рахунку: ");

        BankAccount account = authenticate();
        if (account.getAccountBalance() != 0) {
            throw new NonZeroBalanceException(String.format(Locale.US,
                    "Неможливо видалити рахунок з ненульовим балансом (%.2f)", account.getAccountBalance()));
        }

        accounts.remove(account);
        System.out.println("\nРахунок з userId " + account.getUserId() + " успішно видалено");
    }

    private void deposit() {
        printHeader("Внесення коштів: ");

        BankAccount account = authenticate();
        double amount = readAmount();
        account.deposit(amount);

        System.out.printf(Locale.US, "%nКошти успішно внесено. Поточний баланс: %.2f%n",
                account.getAccountBalance());
    }

    private void withdraw() {
        printHeader("Зняття коштів: ");

        BankAccount account = authenticate();
        double amount = readAmount();
        account.withdraw(amount);

        System.out.printf(Locale.US, "%nКошти успішно знято. Поточний баланс: %.2f%n",
                account.getAccountBalance());
    }

    private void printInfo() {
        printHeader("Інформація щодо рахунку: ");

        BankAccount account = authenticate();

        System.out.println("\nІнформація щодо рахунку: ");
        System.out.println(" - номер рахунку: " + account.getAccountNumber());
        System.out.println(" - ім'я клієнта: " + account.getCustomerName());
        System.out.printf(Locale.US, " - баланс рахунку: %.2f%n", account.getAccountBalance());
        System.out.println(" - тип рахунку: Checking");
        if (account instanceof CheckingAccount checking) {
            System.out.printf(Locale.US, " - денний ліміт зняття коштів: %.2f%n",
                    checking.getDailyWithdrawalLimit());
        }

        System.out.print("\nНатисніть Enter, щоб повернутися до головного меню...");
        scanner.nextLine();
    }

    private void printHeader(String title) {
        System.out.println("\n****************");
        System.out.println(title);
        System.out.println("****************");
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private Long readUserId() {
        String input = readLine(">>> Введіть userId: ");
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new InvalidUserIdException("userId повинен бути цілим числом, отримано: \"" + input + "\"");
        }
    }

    private double readAmount() {
        String input = readLine(">>> Введіть суму: ");
        try {
            return Double.parseDouble(input.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Сума повинна бути числом, отримано: \"" + input + "\"");
        }
    }

    private BankAccount authenticate() {
        Long userId = readUserId();
        String password = readLine(">>> Введіть пароль: ");

        BankAccount account = findAccount(userId);
        if (account == null) {
            throw new AccountNotFoundException("Рахунок з userId " + userId + " не знайдено");
        }
        if (!account.checkPassword(password)) {
            throw new WrongPasswordException("Невірний пароль");
        }
        return account;
    }

    private BankAccount findAccount(Long userId) {
        return accounts.stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}
