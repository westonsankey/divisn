package co.divisn.app;

import co.divisn.destination.DestinationType;
import co.divisn.destination.bigquery.BatchProcessor;
import co.divisn.kafka.Consumer;

public class Main {
    public static void main(String[] args) {
        BatchProcessor batchProcessor = new BatchProcessor();

        Consumer consumer = new Consumer("divisn.destination.bigquery",
                "divisn.destination.bigquery",
                "localhost:9092", batchProcessor,
                60 * 1000,
                DestinationType.BIGQUERY);
        consumer.consume();
    }
}
