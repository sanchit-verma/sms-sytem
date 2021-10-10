package com.meesho.smssystem.dtos.requests.apireq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "msisdn",
        "correlationid"
})
@Generated("jsonschema2pojo")
@Data
public class Destination {

    @JsonProperty("msisdn")
    private List<String> msisdn = null;
    @JsonProperty("correlationid")
    private String correlationid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("msisdn")
    public List<String> getMsisdn() {
        return msisdn;
    }

    @JsonProperty("msisdn")
    public void setMsisdn(List<String> msisdn) {
        this.msisdn = msisdn;
    }

    @JsonProperty("correlationid")
    public String getCorrelationid() {
        return correlationid;
    }

    @JsonProperty("correlationid")
    public void setCorrelationid(String correlationid) {
        this.correlationid = correlationid;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}