
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY gradlew *.gradle *.gradle.kts ./
COPY gradle ./gradle

RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

COPY src ./src
RUN ./gradlew clean bootJar -ParchiveName=app.jar --no-daemon -x test || \
    ./gradlew clean jar -ParchiveName=app.jar --no-daemon -x test


FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

EXPOSE 8080
RUN useradd -m appuser && chown -R appuser:appuser /app
USER appuser
COPY --from=build --chown=appuser:appuser /app/build/libs/app.jar ./app.jar

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=70.0", "-jar", "app.jar"]