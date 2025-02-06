FROM gradle:8.11.1-jdk21 AS builder
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

RUN gradle build || return 0

COPY src ./src
RUN gradle build

RUN unzip build/distributions/componente-auth-0.0.1-SNAPSHOT.zip -d /app/out


FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/out/componente-auth-0.0.1-SNAPSHOT /app/

RUN apk add --no-cache curl

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["/app/bin/componente-auth"]

