# Java Grep Application (Lambda, Streams, and List-Based Implementations)

## Introduction
The Java Grep App is a search utility designed to recursively traverse directories and identify text patterns within files, similar to the Linux grep command. The project includes two implementations to demonstrate different programming approaches in Java 8+. Both implementations utilize Lambdas and the Stream API but differ in how data is processed and stored in memory.

The first implementation, JavaGrepLambdaImp, follows a fully stream-based approach with an end-to-end functional pipeline. The second implementation, JavaGrepLambdaImpStandard, uses an explicit List-based approach combined with Streams for transformation and filtering. The application is built using Core Java, managed with Maven, and uses the SLF4J/Log4j framework for detailed execution logging. By leveraging Java NIO (New I/O) and functional paradigms, the app provides a clean and modular approach to file system traversal and text filtering.

## Quick Start
To use the application, ensure you have Maven and Java 8 installed.

Package the application:

Bash
mvn clean package

Run the application with three arguments:
IntelliJ Idea
Edit config to add 3 arguments "regex" "rootPath" "outFile"
For example:
".*Romeo.*Juliet.*" "./data" "./out/grep.txt"

Bash
java -cp target/JavaGrepProject-1.0-SNAPSHOT.jar \
ca.jrvs.apps.grep.JavaGrepLambdaImp "regex" "rootPath" "outFile"

Example:

Bash
java -cp target/JavaGrepProject-1.0-SNAPSHOT.jar \
ca.jrvs.apps.grep.JavaGrepLambdaImp ".*Romeo.*Juliet.*" "./data" "./out/grep.txt"

## Implementation
The project contains two implementations that solve the same problem using different processing strategies.

### Pseudocode
The stream-based implementation (JavaGrepLambdaImp) orchestrates the workflow using a fully functional stream pipeline:

Plaintext
Method process():
    1. Define the root path and destination file path.
    2. Initiate a Stream of Paths using Files.walk(rootPath).
    3. Filter the stream to include only regular files.
    4. Use flatMap to convert each file into a stream of its internal lines.
    5. Filter lines using the containsPattern(line) method (regex match).
    6. Pass matched lines directly to writeToFile().

The list-based implementation (JavaGrepLambdaImpStandard) follows a more explicit approach:

Plaintext
Method process():
    1. Call listFiles() to eagerly collect all files into a List<File>.
    2. Convert each file into a List<String> using readLines().
    3. Use Streams to flatten file contents into a single stream of lines.
    4. Filter lines using the containsPattern(line) method.
    5. Collect matched lines into a List<String>.
    6. Write the list of matched lines to disk using writeToFile().

## Performance Issue
The primary performance issue arises in the list-based implementation when using Collectors.toList() to store matched lines. If the search produces a very large number of matches, all results are stored in memory, which may lead to an OutOfMemoryError.

The stream-based implementation mitigates this issue by supporting lazy evaluation, allowing matched lines to be written directly to disk without retaining the entire dataset in memory. For large datasets, the stream-based approach is more memory efficient and scalable.

## Test
The application was tested manually through the following process:

Data Preparation: Downloaded a sample dataset of Shakespeare's plays and placed them in a ./data folder.

Manual Execution: Ran the app using ".*Romeo.*Juliet.*" to find lines where both characters appear, and ".*ERROR.*" to identify log-related entries.

Validation: Used the Linux grep -Er command on the same dataset and compared the line counts of the generated outFile to ensure 100% accuracy.

## Deployment
The application is Dockerized for platform-independent distribution:

Docker Image: A Dockerfile was created using an eclipse-temurin:8-jre-alpine base image to keep the footprint small.

Distribution: The application is packaged as a JAR and copied into the container.

Volume Mapping: Users can mount local data and output directories to the container to process files and retrieve results.

## Improvement
Parallel Processing: Apply .parallel() on the file stream to scan multiple files concurrently across CPU cores.

Memory Optimization: Favor the stream-based implementation for large datasets to avoid materializing intermediate lists in memory.

Extended Regex Support: Add a command-line flag to enable optional case-insensitive matching (e.g., (?i)) within the regex logic.
