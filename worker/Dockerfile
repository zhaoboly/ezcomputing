FROM openjdk:8-jdk-alpine

VOLUME /tmp
COPY ./target/ezcomputing-worker-0.1.jar /usr/src/ezcomputing-worker/
ENV TZ=America/Toronto
WORKDIR /usr/src/ezcomputing-worker

ENTRYPOINT ["java","-jar","ezcomputing-worker-0.1.jar"]

