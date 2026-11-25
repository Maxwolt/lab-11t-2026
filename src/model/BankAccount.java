package model;

import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.InvalidCustomerNameException;
import exceptions.InvalidPasswordException;
import exceptions.InvalidUserIdException;

public abstract class BankAccount {

    public static final int MIN_PASSWORD_LENGTH = 8;

    private String customerName;
    private Long accountNumber;
    private Long userId;
    private String userPassword;
    private Double accountBalance;

    protected BankAccount(String customerName, Long accountNumber, Long userId, String userPassword) {
        setCustomerName(customerName);
        setAccountNumber(accountNumber);
        setUserId(userId);
        setUserPassword(userPassword);
        this.accountBalance = 0.0;
    }

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    public boolean checkPassword(String password) {
        return userPassword.equals(password);
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidPasswordException(
                    "Пароль повинен містити щонайменше " + MIN_PASSWORD_LENGTH + " символів");
        }
        if (password.chars().noneMatch(Character::isDigit)) {
            throw new InvalidPasswordException("Пароль повинен містити щонайменше одну цифру");
        }
    }

    protected static void validateAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new InvalidAmountException("Сума повинна бути більшою за 0");
        }
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.isBlank()) {
            throw new InvalidCustomerNameException("Ім'я клієнта не може бути порожнім");
        }
        this.customerName = customerName;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        if (userId == null) {
            throw new InvalidUserIdException("userId не може бути порожнім");
        }
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        validatePassword(userPassword);
        this.userPassword = userPassword;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        if (accountBalance == null || accountBalance < 0) {
            throw new InsufficientFundsException("Баланс рахунку не може бути від'ємним");
        }
        this.accountBalance = accountBalance;
    }
}
