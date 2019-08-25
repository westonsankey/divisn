package co.divisn.kafka;

import co.divisn.destination.DestinationType;
import co.divisn.destination.Processor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Consumer {

    private Logger logger = LoggerFactory.getLogger(co.divisn.kafka.Consumer.class.getName());
    private KafkaConsumer<String, String> consumer;
    private Processor batchProcessor;
    private long bufferMillis;
    private DestinationType type;

    /**
     * Create a new Kafka consumer instance.
     *
     * @param topic            topic to consume messages from
     * @param groupId          group ID of the application
     * @param bootstrapServers connection string to the Kafka broker
     * @param processor        batch processor
     * @param bufferMillis     amount of time to buffer records before processing batch
     * @param type             destination type
     */
    public Consumer(String topic, String groupId, String bootstrapServers, Processor processor,
                    long bufferMillis, DestinationType type) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton(topic));
        this.batchProcessor = processor;
        this.bufferMillis = bufferMillis;
        this.type = type;
    }

    /**
     * Long-running method that continuously polls Kafka for new messages. If messages processing
     * in the batch processor is unsuccessful for a batch, the consumer will be killed.
     */
    public void consume() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(60));
                List<ConsumerRecord> batch = new ArrayList<>();

                for (ConsumerRecord<String, String> record : records) {
                    logger.info("Key: " + record.key() + "\n" +
                                "Partition: " + record.partition() + "\n" +
                                "Offset: " + record.offset());

                    batch.add(record);
                }

                boolean processedBatch = batchProcessor.processBatch(batch);

                if (!processedBatch) {
                    System.out.println("Failed to process batch");
                    System.exit(1);
                } else {
                    consumer.commitAsync();
                }

                // Buffer up records for the specified amount of time
                Thread.sleep(bufferMillis);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            // Since we are using asynchronous commits in the poll loop, we need to ensure that
            // the final commit succeeds.
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}
