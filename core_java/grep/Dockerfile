FROM eclipse-temurin:8-jre-alpine
# This matches your specific JAR name and renames it to grep.jar inside the image
COPY target/JavaGrepProject-1.0-SNAPSHOT.jar /usr/local/app/grep/lib/grep.jar
ENTRYPOINT ["java","-jar","/usr/local/app/grep/lib/grep.jar"]