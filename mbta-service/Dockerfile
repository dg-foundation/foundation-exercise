FROM maven:3.6.3-jdk-11-slim as maven
COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn dependency:go-offline -B
RUN mvn package
FROM openjdk:11-jre-slim

WORKDIR /app
COPY --from=maven target/*SNAPSHOT.jar ./app.jar
EXPOSE 8080

CMD ["java", "-jar", "./app.jar"]