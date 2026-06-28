FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY shared/pom.xml shared/
COPY scheduler/pom.xml scheduler/
COPY worker/pom.xml worker/
COPY task/pom.xml task/
COPY observability/pom.xml observability/
COPY app/pom.xml app/
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY shared/src shared/src
COPY scheduler/src scheduler/src
COPY worker/src worker/src
COPY task/src task/src
COPY observability/src observability/src
COPY app/src app/src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]