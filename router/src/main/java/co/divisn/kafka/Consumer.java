package co.divisn.kafka;

import co.divisn.app.Router;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * Consumes messages from a Kafka stream and routes them to the appropriate endpoint.
 */
public class Consumer {

    private Logger logger = LoggerFactory.getLogger(Consumer.class.getName());
    private KafkaConsumer<String, String> consumer;
    private Router router;

    /**
     * Create a new instance of a Consumer.
     *
     * @param topic             Kafka topic from which to consume
     * @param groupId           consumer group ID
     * @param bootstrapServers  bootstrap servers of the Kafka cluster
     * @param router            object that handles routing of messages
     */
    public Consumer(String topic, String groupId, String bootstrapServers, Router router) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.router = router;
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton(topic));
    }

    /**
     * Continuously read messages from the Kafka topic and route them.
     */
    public void consume() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {
                logger.info("Key: " + record.key() + "\n" +
                            "Partition: " + record.partition() + "\n" +
                            "Offset: " + record.offset());

                // Route message
                router.route(record.value());
            }
        }
    }
}
