//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

  public static void main(String[] args) {
    RegexExc regex = new RegexExcImp();

    System.out.println("--- Testing Jpeg Matcher ---");
    System.out.println("test.jpg:    " + regex.matchJpeg("test.jpg"));    // true
    System.out.println("PHOTO.JPEG:  " + regex.matchJpeg("PHOTO.JPEG"));  // true
    System.out.println("file.png:    " + regex.matchJpeg("file.png"));    // false

    System.out.println("\n--- Testing IP Matcher ---");
    System.out.println("192.168.0.1: " + regex.matchIp("192.168.0.1"));   // true
    System.out.println("999.999.9.9: " + regex.matchIp("999.999.9.9"));   // true
    System.out.println("12.34.56:    " + regex.matchIp("12.34.56"));      // false

    System.out.println("\n--- Testing Empty Line Matcher ---");
    System.out.println("'' (empty):  " + regex.isEmptyLine(""));          // true
    System.out.println("'   ' (tabs):" + regex.isEmptyLine("   \t   "));  // true
    System.out.println("' hello ':   " + regex.isEmptyLine(" hello "));   // false
  }
}