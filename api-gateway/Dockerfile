FROM eclipse-temurin:17-jdk
WORKDIR /app
# скопіюємо перший jar під build/libs
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
