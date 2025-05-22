FROM eclipse-temurin:17-jre AS build
WORKDIR /project

COPY gradlew .
COPY gradle/wrapper gradle/wrapper
COPY settings.gradle .
COPY build.gradle .
COPY src/ src/

RUN chmod +x gradlew \
 && ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /project/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
