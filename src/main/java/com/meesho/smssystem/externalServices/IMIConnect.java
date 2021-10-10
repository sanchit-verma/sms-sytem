package com.meesho.smssystem.externalServices;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.smssystem.dtos.requests.apireq.ApiReqBody;
import com.meesho.smssystem.dtos.requests.apireq.Channels;
import com.meesho.smssystem.dtos.requests.apireq.Destination;
import com.meesho.smssystem.dtos.requests.apireq.Sms;
import com.meesho.smssystem.exceptions.IMIConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class IMIConnect implements MessagingAPI {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${sms.gateway.url}")
    private String URL;
    @Value("${sms.gateway.key}")
    private String SMSGatewayAccessKeyValue;
    @Value("${sms.gateway.access-key-field}")
    private String SMSGatewayAccessKeyField;
    @Value("${sms.gateway.success-code}")
    private String successCode;

    @Autowired
    public IMIConnect(RestTemplate restTemplate, @Qualifier("pojoMapper") ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendSMSRequestToAPI(ApiReqBody apiReqBody) throws JsonProcessingException, URISyntaxException, IMIConnectException {
        URI uri = new URI(URL); // throws exception
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set(SMSGatewayAccessKeyField, SMSGatewayAccessKeyValue);
        HttpEntity<ApiReqBody> request = new HttpEntity<>(apiReqBody, httpHeaders);

        ResponseEntity<String> receivedResponse = restTemplate.postForEntity(uri, request, String.class);

        JsonNode responseNode = objectMapper.readTree(receivedResponse.getBody());
        String statusCode = getResponseStatusCode(responseNode);
        if(!statusCode.equals(successCode)){
            throw new IMIConnectException("Api Error! Fail to Send.");
        }
    }

    private String getResponseStatusCode(JsonNode responseNode) {
        JsonNode responseObject;
        // Response can differ as per results. Check response Structure on imiConnect documentation
        if(responseNode.get("response").isArray()){
            responseObject = responseNode.get("response").get(0);
        }
        else{
            responseObject = responseNode.get("response");
        }
        return responseObject.get("code").textValue();
    }

    @Override
    public ApiReqBody setRequestBodyDetails(String phoneNumber, String message){
        // declaration of dependencies
        ApiReqBody apiReqBody = new ApiReqBody();
        Channels channels = new Channels();
        Sms sms = new Sms();
        Destination destination = new Destination();

        List<String> listPhoneNumber = new ArrayList<>();
        List<Destination> listDestination = new ArrayList<>();

        listPhoneNumber.add(phoneNumber);
        sms.setText(message);
        destination.setCorrelationid("Some Unique Id");
        destination.setMsisdn(listPhoneNumber);
        listDestination.add(destination);
        channels.setSms(sms);

        apiReqBody.setDeliverychannel("sms");
        apiReqBody.setDestination(listDestination);
        apiReqBody.setChannels(channels);

        return apiReqBody;
    }
}
