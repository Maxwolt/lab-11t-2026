package model;

import exceptions.DailyLimitExceededException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;

import java.time.LocalDate;
import java.util.Locale;

public class CheckingAccount extends BankAccount {

    public static final double DEFAULT_DAILY_WITHDRAWAL_LIMIT = 300;
    private static final double EPSILON = 1e-9;

    private double dailyWithdrawalLimit = DEFAULT_DAILY_WITHDRAWAL_LIMIT;
    private double withdrawnToday;
    private LocalDate lastWithdrawalDate;

    public CheckingAccount(String customerName, Long accountNumber, Long userId, String userPassword) {
        super(customerName, accountNumber, userId, userPassword);
    }

    @Override
    public void deposit(double amount) {
        validateAmount(amount);
        setAccountBalance(getAccountBalance() + amount);
    }

    @Override
    public void withdraw(double amount) {
        validateAmount(amount);

        LocalDate today = LocalDate.now();
        if (!today.equals(lastWithdrawalDate)) {
            withdrawnToday = 0;
        }

        if (withdrawnToday + amount > dailyWithdrawalLimit + EPSILON) {
            throw new DailyLimitExceededException(String.format(Locale.US,
                    "Перевищено денний ліміт зняття коштів (%.2f). Сьогодні вже знято %.2f, доступно ще %.2f",
                    dailyWithdrawalLimit, withdrawnToday, dailyWithdrawalLimit - withdrawnToday));
        }
        if (amount > getAccountBalance() + EPSILON) {
            throw new InsufficientFundsException(String.format(Locale.US,
                    "Недостатньо коштів на рахунку. Баланс: %.2f", getAccountBalance()));
        }

        double newBalance = getAccountBalance() - amount;
        setAccountBalance(newBalance < EPSILON ? 0.0 : newBalance);
        withdrawnToday += amount;
        lastWithdrawalDate = today;
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        if (!Double.isFinite(dailyWithdrawalLimit) || dailyWithdrawalLimit <= 0) {
            throw new InvalidAmountException("Денний ліміт повинен бути більшим за 0");
        }
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }
}
