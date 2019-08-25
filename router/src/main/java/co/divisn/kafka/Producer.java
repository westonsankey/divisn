package co.divisn.kafka;

public interface Producer {

    /**
     * Send a message synchronously to Kafka.
     *
     * @param topic    topic to send the message to
     * @param key      message key
     * @param message  message to send
     * @return         true if the message was sent successfully, false otherwise
     */
    boolean SendMessageSynchronous(String topic, String key, String message);
}
