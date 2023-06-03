FROM openjdk:8-jdk-alpine

RUN apk add dumb-init
RUN mkdir /app
RUN mkdir /app/logs
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser

COPY github-auth.jar /app/github-auth.jar

WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
CMD "dumb-init" "java" "-jar" "github-auth.jar"