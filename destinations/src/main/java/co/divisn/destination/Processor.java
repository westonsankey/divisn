package co.divisn.destination;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface Processor {

    boolean processBatch(List<ConsumerRecord> batch);
}
