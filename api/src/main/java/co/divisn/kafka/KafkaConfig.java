package co.divisn.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    private String bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS");

    private String topic = System.getenv("KAFKA_TOPIC");

    private String clientId = System.getenv("KAFKA_CLIENT_ID");

    private int retries = Integer.parseInt(System.getenv("KAFKA_RETRIES"));

    /**
     * Create an instance of a Kafka JsonProducer.
     *
     * @return an instance of a Kafka JsonProducer
     * @throws Exception
     */
    @Bean
    public JsonProducer jsonProducer() throws Exception {
        return new JsonProducer(bootstrapServers, clientId, topic, retries);
    }

}
