package co.divisn.destination.bigquery;

import co.divisn.destination.Processor;
import com.google.cloud.storage.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BatchProcessor implements Processor {

    private Logger logger = LoggerFactory.getLogger(
            co.divisn.destination.bigquery.BatchProcessor.class.getName());

    public boolean processBatch(List<ConsumerRecord> batch) {

        // There should be a better way to do this by writing the bytes of each
        // string to an output stream, but I couldn't get it to work.
        StringBuilder sb = new StringBuilder();
        batch.forEach(record -> sb.append(record.value() + "\n"));

        try {
            Storage storage = StorageOptions.getDefaultInstance().getService();
            UUID fileId = UUID.randomUUID();

            // TODO: don't hardcode date
            String objectName = "2019-07-04/" + fileId.toString() + ".json";
            BlobId blobId = BlobId.of("divisn-source", objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
            Blob blob = storage.create(blobInfo, sb.toString().getBytes(UTF_8));

            batch.forEach(record -> logger.info("Key: " + record.key() + "\n" +
                                                "Partition: " + record.partition() + "\n" +
                                                "Offset: " + record.offset()));

        } catch (Exception ex) {
            logger.error("Error writing batch to Cloud Storage");
            return false;
        }

        return true;
    }
}
