FROM gradle:8.4.0-jdk21 AS build

WORKDIR /usr/app

COPY src ./src
COPY static-content ./static-content
# COPY gradle ./gradle  <-- REMOVE THIS LINE
COPY build.gradle.kts ./
COPY settings.gradle.kts ./

RUN gradle build && gradle jar

FROM eclipse-temurin:21-jre

WORKDIR /usr/app
COPY --from=build /usr/app/ .

EXPOSE 8080
CMD ["java", "-jar", "./build/libs/2425-2-LEIC44D-G12.jar"]
