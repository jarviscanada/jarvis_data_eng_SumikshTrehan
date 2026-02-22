/**
 * Implementation of RegexExc interface using Java Regex APIs.
 */
public class RegexExcImp implements RegexExc {

  /**
   * Matches jpg or jpeg extensions (case-insensitive).
   */
  @Override
  public boolean matchJpeg(String filename) {
    // (?i) makes the match case-insensitive
    // .+\. ensures there is a filename before the literal dot
    // jpe?g allows for both 'jpg' and 'jpeg'
    return filename.matches("(?i).+\\.jpe?g");
  }

  /**
   * Matches a simplified IP address (0.0.0.0 to 999.999.999.999).
   */
  @Override
  public boolean matchIp(String ip) {
    // (\\d{1,3}\\.){3} matches three sets of 1-3 digits followed by a dot
    // \\d{1,3} matches the final set of digits
    return ip.matches("(\\d{1,3}\\.){3}\\d{1,3}");
  }

  /**
   * Returns true if the line is empty or contains only whitespace.
   */
  @Override
  public boolean isEmptyLine(String line) {
    // ^ and $ anchor the search to the start and end
    // \\s* matches zero or more whitespace characters (spaces, tabs, etc.)
    return line.matches("^\\s*$");
  }
}