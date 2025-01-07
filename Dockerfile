FROM maven:3-openjdk-17 as maven1
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package


FROM openjdk:17
WORKDIR /app
COPY --from=maven1 /app/target/freelance-auth-service-0.0.1-SNAPSHOT.jar /app
EXPOSE 8081
CMD ["java","-jar","/app/freelance-auth-service-0.0.1-SNAPSHOT.jar"]