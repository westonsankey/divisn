package co.divisn.destination.elasticsearch;

import co.divisn.destination.Processor;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class BatchProcessor implements Processor {
    private Logger logger = LoggerFactory.getLogger(
            co.divisn.destination.elasticsearch.BatchProcessor.class.getName());

    private RestHighLevelClient client;

    public BatchProcessor() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));
    }

    public boolean processBatch(List<ConsumerRecord> batch) {
        for (ConsumerRecord record : batch) {
            IndexRequest indexRequest = new IndexRequest("track").source(record.value(), XContentType.JSON);

            try {
                IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                logger.info("Document ID: " + indexResponse.getId());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }
}
