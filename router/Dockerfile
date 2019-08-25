FROM openjdk:8-jre-alpine

WORKDIR /app
COPY target/router-1.0-SNAPSHOT-jar-with-dependencies.jar router/app.jar
EXPOSE 8080
CMD ["/usr/bin/java", "-jar", "router/app.jar"]
