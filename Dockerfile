FROM openjdk:8-jre-slim

RUN mkdir -p /var/service/config
COPY ./target/sparkjava-simpleServer-1.0-SNAPSHOT.jar /home

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home/sparkjava-simpleServer-1.0-SNAPSHOT.jar"]
