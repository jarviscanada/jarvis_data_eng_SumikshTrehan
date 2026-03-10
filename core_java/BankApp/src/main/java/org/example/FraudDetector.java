package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class FraudDetector {

  public List<Integer> detectFraud(List<Integer> transactions, int threshold) {
    List<Integer> sus = new ArrayList<>();

    for (Integer amount : transactions) {
      if (amount > threshold) {
        sus.add(amount);
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
