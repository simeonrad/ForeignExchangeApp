FROM gradle:jdk17 as build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17.0.1-jdk-slim
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar /app/
ENTRYPOINT ["java", "-jar", "/app/ForeignExchangeApp-0.0.1-SNAPSHOT.jar"]