package ca.jrvs.apps.practice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LambdaStreamImpTest {

  private LambdaStreamImp lse;

  @BeforeEach
  void setUp() {
    lse = new LambdaStreamImp();
  }

  @Test
  void createStrStream() {
    Stream<String> stream = lse.createStrStream("a", "b", "c");
    List<String> result = stream.collect(Collectors.toList());
    assertEquals(3, result.size());
    assertEquals("b", result.get(1));
  }

  @Test
  void toUpperCase() {
    List<String> result = lse.toUpperCase("hello", "world")
        .collect(Collectors.toList());
    assertEquals("HELLO", result.get(0));
    assertEquals("WORLD", result.get(1));
  }

  @Test
  void filter() {
    Stream<String> names = Stream.of("Apple", "Banana", "Cherry");
    List<String> result = lse.filter(names, "a").collect(Collectors.toList());
    // "Apple" (contains 'p'), "Banana" (contains 'a'), "Cherry" (contains 'e')
    // Note: contains is case sensitive. "Apple" stays, "Banana" filtered.
    assertTrue(result.contains("Apple"));
    assertFalse(result.contains("Banana"));
  }

  @Test
  void createIntStream() {
    int[] nums = {1, 2, 3};
    IntStream stream = lse.createIntStream(nums);
    assertEquals(6, stream.sum());
  }

  @Test
  void toList() {
    Stream<String> stream = Stream.of("x", "y");
    List<String> list = lse.toList(stream);
    assertEquals(2, list.size());
  }

  @Test
  void createIntStreamRange() {
    IntStream range = lse.createIntStream(1, 5);
    List<Integer> list = lse.toList(range);
    assertEquals(5, list.size());
    assertEquals(5, list.get(4)); // Inclusive check
  }

  @Test
  void squareRootIntStream() {
    IntStream nums = IntStream.of(4, 9);
    double[] results = lse.squareRootIntStream(nums).toArray();
    assertEquals(2.0, results[0]);
    assertEquals(3.0, results[1]);
  }

  @Test
  void getOdd() {
    IntStream nums = IntStream.of(1, 2, 3, 4, 5);
    List<Integer> odds = lse.toList(lse.getOdd(nums));
    assertEquals(Arrays.asList(1, 3, 5), odds);
  }

  @Test
  void flatNestedInt() {
    List<List<Integer>> nested = Arrays.asList(
        Arrays.asList(1, 2),
        Arrays.asList(3)
    );
    List<Integer> squares = lse.flatNestedInt(nested.stream())
        .collect(Collectors.toList());
    assertEquals(Arrays.asList(1, 4, 9), squares);
  }
}