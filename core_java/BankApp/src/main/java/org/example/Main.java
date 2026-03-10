package org.example;

import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    // regular adding amounts
    BankAccount myAccount = new BankAccount("1", "Sumiksh", 100.0);
    myAccount.deposit(150.0);
    myAccount.withdraw(30.0);
    System.out.println("--- Account Summary Without (Regular) Streams ---");
    System.out.println(myAccount.getAccountInfo());

    // Fraud
    FraudDetector detector = new FraudDetector();
    List<Integer> txns = Arrays.asList(20, 40, 5000, 30);
    List<Integer> fraud = detector.detectFraud(txns, 1000);

    // Streams
    System.out.println("Suspicious Transactions: " + fraud);
    System.out.println("\n--- Account Summary Streams ---");
    System.out.println(myAccount.getAccountInfo());
    System.out.println("Total Amount Deposited: $" + myAccount.getTotalDeposited());
    System.out.println("Total Amount Withdrawn: $" + myAccount.getTotalWithdrawn());
    System.out.println("Largest Single Transaction: $" + myAccount.getLargestTransaction());

    // 5. List all individual deposit amounts
    System.out.println("All Deposits: " + myAccount.getAllDepositAmounts());
    List<Integer> fraudStream = FraudDetector.streamDetectFraud(txns, 1000);
    System.out.println("Suspicious Stream (Fraud) Transactions: " + fraud);

    // 6. Verify final balance consistency
    double calculatedBalance = myAccount.getTotalDeposited() - myAccount.getTotalWithdrawn();
    System.out.println("Verified Balance: $" + calculatedBalance);
  }
}