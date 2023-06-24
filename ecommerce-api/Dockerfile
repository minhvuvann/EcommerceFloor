FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:11-jdk-slim
COPY --from=build /target/ecommerce-api.jar ecommerce-api.jar
EXPOSE 8202

ENTRYPOINT ["java","-jar","ecommerce-api.jar"]

