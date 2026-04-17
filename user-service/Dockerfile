# FROM eclipse-temurin:17-jre-alpine
# WORKDIR /app
# COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .
RUN apk add --no-cache maven
RUN mvn clean package -DskipTests
RUN cp target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]