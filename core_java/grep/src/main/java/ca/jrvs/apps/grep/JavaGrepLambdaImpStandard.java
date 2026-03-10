package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepLambdaImpStandard extends JavaGrepImp {
  final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaImpStandard.class);

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrepLambdaImpStandard regex rootPath outFile");
    }

    // Fixes the "No appenders found" warning
    BasicConfigurator.configure();

    JavaGrepLambdaImpStandard grepLambda = new JavaGrepLambdaImpStandard();
    grepLambda.setRegex(args[0]);
    grepLambda.setRootPath(args[1]);
    grepLambda.setOutFile(args[2]);

    try {
      // This calls the OVERRIDDEN process method below
      grepLambda.process();
    } catch (Exception ex) {
      grepLambda.logger.error("Error: Unable to process", ex);
    }
  }

  @Override
  public void process() throws IOException {
    // 1. Explicitly calling listFiles() which returns a List<File>
    List<File> files = listFiles(getRootPath());

    // 2. Using Stream API to flatMap those files into lines
    List<String> matchedLines = files.stream()
        .flatMap(file -> readLines(file).stream()) // Calling readLines() here
        .filter(this::containsPattern)
        .collect(Collectors.toList());
    logger.info("Matched lines in JavaGrepLambdaImpStandard :{}", matchedLines);
    // 3. Explicitly calling writeToFile() to save results
    writeToFile(matchedLines);

    logger.info("Process completed. Found {} matches in JavaGrepLambdaImpStandard.", matchedLines.size());
  }

  @Override
  public List<File> listFiles(String rootDir) {
    try (Stream<Path> pathStream = Files.walk(Paths.get(rootDir))) {
      return pathStream
          .filter(Files::isRegularFile)
          .peek(path -> logger.info("Match found for processing JavaGrepLambdaImpStandard: {}", path.getFileName()))
          .map(Path::toFile)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Failed to walk directory JavaGrepLambdaImpStandard: " + rootDir, e);
    }
  }

  @Override
  public List<String> readLines(File inputFile) {
    try (Stream<String> lineStream = Files.lines(inputFile.toPath())) {
      return lineStream.collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Failed to read file: " + inputFile.getName(), e);
    }
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    File out = new File(getOutFile());
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
      for (String line : lines) {
        bw.write(line);
        bw.newLine();
      }
    }
  }
}