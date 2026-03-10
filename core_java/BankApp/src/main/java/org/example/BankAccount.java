package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
class BankAccount {
  private String accountNumber;
  private String accountHolder;
  private double balance;

  // Separate lists remove the need for an Enum "Type"
  private List<Double> deposits = new ArrayList<>();
  private List<Double> withdrawals = new ArrayList<>();

  public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
    // Check for negative starting balance
    if (initialBalance < 0) {
      throw new IllegalArgumentException("Initial balance cannot be negative: $" + initialBalance);
    }
    this.accountNumber = accountNumber;
    this.accountHolder = accountHolder;
    this.balance = initialBalance;
    this.deposits.add(initialBalance);
  }

  public void deposit(double amount) {
    balance += amount;
    deposits.add(amount);
  }

  public void withdraw(double amount) {
    // 1. Check if the input amount is valid
    if (amount <= 0) {
      throw new IllegalArgumentException("Withdrawal amount must be positive: $" + amount);
    }
    // 2. Check if the account has enough money
    if (amount > balance) {
      throw new IllegalStateException("Insufficient funds. Current balance: $" + balance +
          ", Attempted withdrawal: $" + amount);
    }
    balance -= amount;
    withdrawals.add(amount);
  }

  // Streams
  public double getTotalDeposited() {
    return deposits.stream()
        .mapToDouble(Double::doubleValue)
        .sum();
  }


  public double getTotalWithdrawn() {
    return withdrawals.stream()
        .mapToDouble(Double::doubleValue)
        .sum();
  }

  public double getLargestTransaction() {
    List<Double> allTransactions = new ArrayList<>(deposits);
    allTransactions.addAll(withdrawals);

    return allTransactions.stream()
        .mapToDouble(Double::doubleValue)
        .max()
        .orElse(0.0);
  }

  // 4. List of all individual deposit amounts
  public List<Double> getAllDepositAmounts() {
    return deposits.stream()
        .collect(Collectors.toList());
  }

  public String getAccountInfo() {
    return "Account: " + accountNumber + " | Holder: " + accountHolder + " | Balance: $" + balance;
  }
}