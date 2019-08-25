FROM openjdk:8-jre-alpine

WORKDIR /app
COPY target/api-2.1.4.RELEASE.jar api/app.jar
EXPOSE 8080
CMD ["/usr/bin/java", "-jar", "api/app.jar"]
