FROM openjdk:8-jdk
VOLUME /tmp
ADD target/*run.jar app.jar
EXPOSE 8080
ENTRYPOINT java -jar /app.jar