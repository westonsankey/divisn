package co.divisn.app;

import co.divisn.destination.DestinationLookup;
import co.divisn.kafka.Consumer;
import co.divisn.kafka.JsonProducer;
import co.divisn.kafka.Producer;

public class Main {

    public static void main(String[] args) {
        String bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS");
        String topic = System.getenv("KAFKA_TOPIC");
        String clientId = System.getenv("KAFKA_CLIENT_ID");
        String groupId = System.getenv("KAFKA_GROUP_ID");
        int retries = Integer.parseInt(System.getenv("KAFKA_RETRIES"));

        Producer kafkaProducer = new JsonProducer(bootstrapServers, clientId, retries);
        DestinationLookup destinationLookup = DestinationLookup.getInstance();
        Router router = new Router(kafkaProducer, destinationLookup);

        Consumer kafkaConsumer = new Consumer(topic, groupId, bootstrapServers, router);
        kafkaConsumer.consume();
    }
}
