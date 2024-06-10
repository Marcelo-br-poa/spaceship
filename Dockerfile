FROM adoptopenjdk/openjdk17:alpine-jre
WORKDIR /app
COPY target/spaceship-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]