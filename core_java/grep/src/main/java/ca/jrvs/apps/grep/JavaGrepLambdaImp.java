package ca.jrvs.apps.grep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

public class JavaGrepLambdaImp extends JavaGrepImp {
  final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaImp.class);

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrepLambdaImp regex rootPath outFile");
    }

    // Configure default log4j settings
    BasicConfigurator.configure();

    JavaGrepLambdaImp grepLambda = new JavaGrepLambdaImp();
    grepLambda.setRegex(args[0]);
    grepLambda.setRootPath(args[1]);
    grepLambda.setOutFile(args[2]);

    try {
      grepLambda.process();
    } catch (Exception ex) {
      grepLambda.logger.error("Error: Unable to process using Lambda", ex);
    }
  }

  @Override
  public void process() throws IOException {
    // Advanced: Using Streams for the entire workflow to optimize memory
    try (Stream<Path> rootStream = Files.walk(Paths.get(getRootPath()))) {
      List<String> matchedLines = rootStream
          .filter(Files::isRegularFile)
          .flatMap(path -> {
            try {
              return Files.lines(path); // Lazy reading
            } catch (IOException e) {
              throw new RuntimeException("Failed to read file: " + path, e);
            }
          })
          .filter(this::containsPattern)
          .peek(line -> logger.info("Matched line found: {}", line))
          .collect(Collectors.toList());

      writeToFile(matchedLines);
    }
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    // Calling the parent's logic but suppressing its internal logging
    // isn't possible without rewriting the method here.
    File out = new File(getOutFile());
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
      for (String line : lines) {
        bw.write(line);
        bw.newLine();
      }
    }
    // Now this will only log once at the end using the Child's logger
    logger.info("Done writing {} lines.", lines.size());
  }
}