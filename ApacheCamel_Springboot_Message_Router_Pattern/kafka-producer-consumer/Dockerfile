FROM java:8
EXPOSE 8085
ADD target/kafka-producer-consumer-example-0.0.1-SNAPSHOT.jar kafka-producer-consumer-example-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","kafka-producer-consumer-example-0.0.1-SNAPSHOT.jar"]

#mvn clean install
#docker build -f Dockerfile -t kafka-producer-consumer-example .
#docker images
#docker compose -f ./kafka-config/docker-compose.yml up -d
#docker run --name kafka-producer-consumer-example --rm -p 8080:8080 kafka-producer-consumer-example


