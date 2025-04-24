FROM maven:3.9.9-amazoncorretto-21-alpine as build
WORKDIR /app
COPY . .
RUN mvn clean install

FROM openjdk:21-jdk-oracle
WORKDIR /app
COPY --from=build /app/target/SpringToDo-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8080
CMD ["java", "-jar", "SpringToDo-0.0.1-SNAPSHOT.jar"]