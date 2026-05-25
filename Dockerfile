# --- Стадия сборки ---
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Копируем сначала только файлы сборки для эффективного кеширования слоев Docker
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

# Задаем переменные окружения для сборщика.
# Теперь они гарантированно применятся и к клиенту, и к daemon-процессу.
ENV GRADLE_OPTS="-Xms64m -Xmx512m -Dorg.gradle.jvmargs=-Xms64m -Xmx512m"

# Скачиваем зависимости (кешируем этот слой)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Копируем исходный код и собираем проект
COPY src ./src
RUN ./gradlew clean bootJar --no-daemon -x test

# --- Стадия запуска ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

EXPOSE 8080

# Создаем некорневого пользователя для безопасности (best practice в продакшене)
RUN useradd -m appuser && chown -R appuser:appuser /app
USER appuser

# Копируем собранный jar-ник (используем bootJar, чтобы имя файла было предсказуемым)
COPY --from=build --chown=appuser:appuser /app/build/libs/*[0-9].jar app.jar

# Динамическое управление памятью на основе лимитов контейнера
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=70.0", "-jar", "app.jar"]