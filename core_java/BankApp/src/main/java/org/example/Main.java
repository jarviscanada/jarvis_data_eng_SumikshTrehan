package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    // Starting with $1000 balance as an example
    BankAccount account = new BankAccount("12345", "Alice", 0.0);
    FraudDetector detector = new FraudDetector();

    while (true) {
      System.out.println("\n--- Banking & Fraud System ---");
      System.out.println("1. Deposit");
      System.out.println("2. Withdraw");
      System.out.println("3. Detect Fraud (Without Streams)");
      System.out.println("4. Detect Fraud (With Streams)");
      System.out.println("5. Get Total Deposited");
      System.out.println("6. Get Total Withdrawn");
      System.out.println("7. Get Largest Transaction");
      System.out.println("8. Get Account Summary");
      System.out.println("9. Exit");
      System.out.print("Select: ");

      int choice = scanner.nextInt();

      if (choice == 9) break;

      switch (choice) {
        case 1:
          double dAmt;
          while (true) {
            System.out.print("Deposit amount: ");
            dAmt = scanner.nextDouble();
            if (dAmt > 0) break;
            System.out.println("Negative amounts not allowed. Re-enter:");
          }
          account.deposit(dAmt);
          break;

        case 2:
          double wAmt;
          while (true) {
            System.out.print("Withdrawal amount: ");
            wAmt = scanner.nextDouble();
            if (wAmt > 0) break;
            System.out.println("Negative amounts not allowed. Re-enter:");
          }
          try {
            account.withdraw(wAmt);
          } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
          }
          break;

        case 3: // Using the instance method detectFraud
          System.out.print("Threshold: ");
          int t1 = scanner.nextInt();
          List<Integer> list1 = new ArrayList<>();
          for (Double d : account.getAllDepositAmounts()) {
            list1.add(d.intValue());
          }
          System.out.println("Suspects: " + detector.detectFraud(list1, t1));
          break;

        case 4: // Using the static method streamDetectFraud
          System.out.print("Threshold: ");
          int t2 = scanner.nextInt();
          List<Integer> list2 = account.getAllDepositAmounts().stream()
              .map(Double::intValue)
              .collect(Collectors.toList());
          System.out.println("Suspects: " + FraudDetector.streamDetectFraud(list2, t2));
          break;

        case 5: // Uses your getTotalDeposited()
          System.out.println("Total Deposited: " + account.getTotalDeposited());
          break;

        case 6: // Uses your getTotalWithdrawn()
          System.out.println("Total Withdrawn: " + account.getTotalWithdrawn());
          break;

        case 7: // Uses your getLargestTransaction()
          System.out.println("Largest: " + account.getLargestTransaction());
          break;

        case 8: // Uses your getAccountInfo()
          System.out.println("Account Info: " + account.getAccountInfo());
          break;


        default:
          System.out.println("Invalid choice.");
      }
    }
    scanner.close();
  }
}
