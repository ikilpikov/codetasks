FROM maven:3.8.4-openjdk-11 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/codetasks-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY --from=builder /app/${JAR_FILE} app.jar  
ENTRYPOINT ["java","-jar","app.jar"]
