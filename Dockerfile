# Базовый образ с Java 8 (Eclipse Temurin — официальная замена openjdk:8)
FROM eclipse-temurin:8-jre

# Рабочая директория внутри контейнера
WORKDIR /app

# Копируем готовый JAR-файл (собирается локально через IDEA)
COPY target/Internet_shop_NIC-0.0.1-SNAPSHOT.jar app.jar

# Экспонируем порт приложения
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]