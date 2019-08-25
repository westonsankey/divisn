package co.divisn.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class JsonProducer implements Producer {

    private KafkaProducer<String, String> producer;

    // Kafka topic the messages will be sent to
    private String topic;

    // Number of times to retry sending the message if it fails
    private int retries;

    /**
     * Create a new instance of a Kafka producer client.
     *
     * @param bootstrapServers comma separated list of brokers
     * @param clientID         name of the producer client
     */
    public JsonProducer(String bootstrapServers, String clientID, String topic, int retries) {
        Properties kafkaProperties = new Properties();

        // Set standard properties
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        kafkaProperties.put(ProducerConfig.CLIENT_ID_CONFIG, clientID);
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        // Set properties to make producer "safe"
        kafkaProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        kafkaProperties.put(ProducerConfig.ACKS_CONFIG, "all");
        kafkaProperties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        kafkaProperties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(kafkaProperties);
        this.topic = topic;
        this.retries = retries;
    }

    /**
     * Send a message synchronously. If the send fails, retry the specified number
     * of times.
     *
     * @param key      key of the message to send
     * @param message  message to send
     * @return true if the message was sent, false otherwise
     */
    public boolean SendMessageSynchronous(String key, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
        Future<RecordMetadata> result = producer.send(record);
        try {
            if (!result.get().hasOffset()) {
                for (int i = 0; i < retries; i++ ) {
                    result = producer.send(record);
                    if (result.get().hasOffset()) {
                        return true;
                    }
                }
                return false;
            }
        } catch (InterruptedException ex) {
            // TODO: handle exception - ensure we don't lose the message
        } catch (ExecutionException ex) {
            // TODO: handle exception - ensure we don't lose message
        }

        return true;
    }
}
