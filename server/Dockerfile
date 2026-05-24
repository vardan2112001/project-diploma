# ==============================================================================
# STAGE 1: BUILD
# Используем официальный образ Maven с Java 17 для сборки приложения
# ==============================================================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Копируем файл конфигурации зависимостей pom.xml
COPY pom.xml .

# Скачиваем все зависимости проекта. Этот слой кэшируется,
# благодаря чему повторные сборки будут проходить мгновенно, если pom.xml не менялся.
RUN mvn dependency:go-offline -B

# Копируем исходный код приложения
COPY src ./src

# Копируем файл данных players.csv, который ты уже закинул в папку server
COPY players.csv ./players.csv

# Собираем исполняемый fat-JAR пакет, пропуская тесты (так как базы данных на этапе сборки еще нет)
RUN mvn package -DskipTests -B

# ==============================================================================
# STAGE 2: RUNTIME
# Минималистичный легковесный образ только с JRE 17 для запуска приложения (~100MB)
# ==============================================================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# Безопасность: создаем системную группу и пользователя, чтобы не запускать контейнер из-под root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Копируем собранный JAR-артефакт из стадии сборки (builder)
COPY --from=builder /build/target/*.jar app.jar

# Создаем директорию для данных и копируем туда наш players.csv
RUN mkdir -p /app/data
COPY --from=builder /build/players.csv /app/data/players.csv

# Передаем права на файлы нашему созданному пользователю appuser
RUN chown -R appuser:appgroup /app

# Переключаемся на безопасного пользователя
USER appuser

# Открываем порт бэкенда (Spring Boot по умолчанию)
EXPOSE 8080

# Проверка работоспособности (Healthcheck) контейнера каждые 30 секунд
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/dashboard || exit 1

# Оптимизированные флаги запуска JVM для Docker-контейнеров
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]