FROM maven:3.9.9-amazoncorretto-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B \
    -DskipTests=true \
    -DskipITs=true \
    -Dmaven.test.skip=true
COPY src src
RUN mvn clean install \
    -DskipTests=true \
    -DskipITs=true \
    -Dmaven.test.skip=true \
    -Dcheckstyle.skip=true \
    -Dspotbugs.skip=true

FROM openjdk:21-jdk-oracle
WORKDIR /app
COPY --from=build /app/target/SpringToDo-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8080
CMD ["java", "-jar", "SpringToDo-0.0.1-SNAPSHOT.jar"]