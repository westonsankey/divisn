package co.divisn.api.event;

import java.util.Date;
import java.util.Map;

public class Track {

    private int sourceId;

    private String messageId;

    private String anonymousId;

    private Date sentAt;

    private String event;

    private Map<String, Object> properties;

    public Track(int sourceId, String messageId, String anonymousId, Date sentAt,
                 String event, Map<String, Object> properties) {
        this.sourceId = sourceId;
        this.messageId = messageId;
        this.anonymousId = anonymousId;
        this.sentAt = sentAt;
        this.event = event;
        this.properties = properties;
    }

    public Track() {}

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAnonymousId() {
        return anonymousId;
    }

    public void setAnonymousId(String anonymousId) {
        this.anonymousId = anonymousId;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
