package co.divisn.app;

import co.divisn.destination.DestinationLookup;
import co.divisn.kafka.Producer;
import co.divisn.destination.DestinationConfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Routes messages based on metadata contained within the message.
 */
public class Router {

    private Logger logger = LoggerFactory.getLogger(Router.class.getName());
    private Producer kafkaProducer;
    private ObjectMapper mapper = new ObjectMapper();
    private DestinationLookup destinationLookup;

    /**
     * Create a new instance.
     *
     * @param kafkaProducer      producer that will send messages to Kakfa
     * @param destinationLookup  cache of source/destination combinations
     */
    public Router(Producer kafkaProducer, DestinationLookup destinationLookup) {
        this.kafkaProducer = kafkaProducer;
        this.destinationLookup = destinationLookup;
    }

    /**
     * Send a message to a Kafka topic based on metadata within the message
     * and the external source/destination lookup. If the message body does
     * not include a sourceId field, it will not be routed anywhere.
     *
     * @param message the message to route
     */
    public void route(String message) {

        try {
            JsonNode rootNode = mapper.readTree(message);
            if (!rootNode.has("sourceId")) {
                logger.error("No sourceId in message body");
                return;
            }

            int sourceID = rootNode.get("sourceId").asInt();

            List<DestinationConfig> destinations = destinationLookup.getDestinations(sourceID);

            // TODO: run this concurrently?
            for (DestinationConfig dest : destinations) {
                kafkaProducer.SendMessageSynchronous(dest.getKafkaTopic(), null, message);
            }

        } catch (IOException ex) {
            logger.error("Error unmarshalling JSON", ex);
        }
    }
}
