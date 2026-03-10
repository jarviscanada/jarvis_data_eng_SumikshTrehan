package ca.jrvs.apps.grep;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

public class JavaGrepImp implements JavaGrep {

  // Final logger using slf4j as required
  final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    // Check for exactly 3 arguments: regex, rootPath, outFile
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    // Configure default log4j settings
    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      // Start the high-level workflow
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error("Error: Unable to process", ex);
    }
  }

  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();
    // Step-by-step workflow: Find files -> Read lines -> Match pattern -> Save
    for (File file : listFiles(getRootPath())) {
      logger.info("get Root Path: {}", getRootPath());
      logger.info("Processing file: {}", file.getName());
      for (String line : readLines(file)) {
        if (containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }
    logger.info("Matched lines found: {}", matchedLines);
    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> fileList = new ArrayList<>();
    File root = new File(rootDir);

    if (!root.exists() || !root.isDirectory()) {
      logger.error("Invalid root directory: " + rootDir);
      return fileList;
    }

    File[] files = root.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          // Recursively search sub-directories
          logger.info("Diving into directory: {}", file.getAbsolutePath());
          fileList.addAll(listFiles(file.getAbsolutePath()));
        } else {
          fileList.add(file);
        }
      }
    }
    return fileList;
  }

  @Override
  public List<String> readLines(File inputFile) {
    if (!inputFile.isFile()) {
      throw new IllegalArgumentException("Target is not a file: " + inputFile.getName());
    }

    List<String> lines = new ArrayList<>();
    // Use BufferedReader for efficient reading
    try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
      String line;
      while ((line = br.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException e) {
      logger.error("Failed to read file: " + inputFile.getName(), e);
    }
    return lines;
  }

  @Override
  public boolean containsPattern(String line) {
    // Matches the line against the user-provided regex
    //logger.info(String.valueOf(Pattern.compile(getRegex()).matcher(line).find()));
    return Pattern.compile(getRegex()).matcher(line).find();
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    File out = new File(getOutFile());
    // Use BufferedWriter and FileWriter to save the results
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
      for (String line : lines) {
        logger.info("Writing line to file: {}", line);
        bw.write(line);
        bw.newLine();
      }
    }
  }

  // --- Getters and Setters for Private Members ---
  @Override public String getRootPath() { return rootPath; }
  @Override public void setRootPath(String rootPath) { this.rootPath = rootPath; }
  @Override public String getRegex() { return regex; }
  @Override public void setRegex(String regex) { this.regex = regex; }
  @Override public String getOutFile() { return outFile; }
  @Override public void setOutFile(String outFile) { this.outFile = outFile; }
}