FROM eclipse-temurin:24-jdk-ubi10-minimal
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]