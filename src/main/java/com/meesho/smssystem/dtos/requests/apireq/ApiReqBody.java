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
        "deliverychannel",
        "channels",
        "destination"
})
@Generated("jsonschema2pojo")
@Data
public class ApiReqBody {

    @JsonProperty("deliverychannel")
    private String deliverychannel;
    @JsonProperty("channels")
    private Channels channels;
    @JsonProperty("destination")
    private List<Destination> destination = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("deliverychannel")
    public String getDeliverychannel() {
        return deliverychannel;
    }

    @JsonProperty("deliverychannel")
    public void setDeliverychannel(String deliverychannel) {
        this.deliverychannel = deliverychannel;
    }

    @JsonProperty("channels")
    public Channels getChannels() {
        return channels;
    }

    @JsonProperty("channels")
    public void setChannels(Channels channels) {
        this.channels = channels;
    }

    @JsonProperty("destination")
    public List<Destination> getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public void setDestination(List<Destination> destination) {
        this.destination = destination;
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