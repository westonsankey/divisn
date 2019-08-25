package co.divisn.kafka;

public interface Producer {

    /**
     * Send a message synchronously to Kafka.
     *
     * @param key      the message key
     * @param message  the message to send
     * @return         true if the message was sent successfully, false otherwise
     */
    boolean SendMessageSynchronous(String key, String message);
}
