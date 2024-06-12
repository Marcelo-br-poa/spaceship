FROM ibm-semeru-runtimes:open-21-jdk-jammy
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven apt-transport-https ca-certificates curl openjdk-11-jre-headless
RUN apt-get clean && apt-get update && apt-get install -y maven
RUN mvn package
COPY target/spaceship-0.0.1-SNAPSHOT.jar /app/spaceship.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/spaceship.jar"]
