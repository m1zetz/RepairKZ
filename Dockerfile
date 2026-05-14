FROM gradle:8.5-jdk17 AS build
COPY . /app
WORKDIR /app
RUN chmod +x gradlew
RUN GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx512m -Xms64m" \
    ./gradlew clean build --no-daemon -x test

FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=60.0", "-jar", "/app.jar"]