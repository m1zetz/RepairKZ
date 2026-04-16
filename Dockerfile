# Этап 1: Сборка
FROM gradle:8.5-jdk17 AS build
# Копируем всё в корень для простоты
COPY . /app
WORKDIR /app
# Даем права на выполнение (на всякий случай)
RUN chmod +x gradlew
# Собираем через wrapper — это надежнее
RUN ./gradlew clean build --no-daemon -x test

# Этап 2: Запуск
FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "/app.jar"]