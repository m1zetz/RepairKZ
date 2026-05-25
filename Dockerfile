# --- Стадия сборки ---
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Копируем абсолютно всё, что нужно для сборки (включая wrapper и настройки)
# Docker сам поймет, изменилось ли что-то в конфигурации
COPY gradlew *.gradle *.gradle.kts ./
COPY gradle ./gradle

# Проверяем зависимости и кешируем их
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Копируем исходный код и собираем
COPY src ./src
RUN ./gradlew clean bootJar --no-daemon -x test

# --- Стадия запуска (остается без изменений) ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
EXPOSE 8080
RUN useradd -m appuser && chown -R appuser:appuser /app
USER appuser
COPY --from=build --chown=appuser:appuser /app/build/libs/*[0-9].jar app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=70.0", "-jar", "app.jar"]