package co.divisn.destination;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Table(name = "destination_config", schema = "router")
public class DestinationConfig implements Serializable {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "source_id")
    private int sourceId;

    @Column(name = "kafka_topic")
    private String kafkaTopic;

    public DestinationConfig() {
    }

    public DestinationConfig(int id, int sourceId, String kafkaTopic) {
        this.id = id;
        this.sourceId = sourceId;
        this.kafkaTopic = kafkaTopic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }
}
