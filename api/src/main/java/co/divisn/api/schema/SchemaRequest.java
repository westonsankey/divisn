package co.divisn.api.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SchemaRequest {

    @JsonProperty("$schema")
    private String schema;

    @JsonProperty("$id")
    private String id;

    private String name;

    private String type;

    private Map<String, Object> properties;

    private boolean additionalProperties;

    public SchemaRequest() {
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public boolean isAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(boolean additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
