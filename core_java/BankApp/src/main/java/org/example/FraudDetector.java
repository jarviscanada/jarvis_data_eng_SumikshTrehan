package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class FraudDetector {

  public List<Integer> detectFraud(List<Integer> transactions, int threshold) {
    List<Integer> sus = new ArrayList<>();

    // Debug: Check if the list actually has data
    System.out.println("DEBUG: FraudDetector received " + transactions.size() + " transactions.");

    // 2. Loop through each transaction
    for (Integer amount : transactions) {
      // Debug: See what value the loop is currently checking
      System.out.println("DEBUG: Checking amount: " + amount + " against threshold: " + threshold);

      if (amount > threshold) {
        sus.add(amount);
        System.out.println("DEBUG: Found Fraud! Adding " + amount);
      }
    }
    return sus;
  }

  public static List<Integer> streamDetectFraud(List<Integer> transactions, int threshold) {
    return transactions.stream()
        .filter(amount -> amount > threshold)
        .collect(Collectors.toList());
  }
}
