package com.meesho.smssystem.externalServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meesho.smssystem.dtos.requests.apireq.ApiReqBody;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class IMIConnectTest {

    @Mock
    RestTemplate restTemplate;
    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    IMIConnect imiConnect;

    private String testPhoneNumber = "1234";
    private String testMessage = "Test Hello!";



    @Test
    void sendSMSRequestToAPI() {
        ResponseEntity<String> testResponse = new ResponseEntity<String>("1001", HttpStatus.CREATED);

        Mockito.when(restTemplate.postForEntity("url","hello", String.class)).thenReturn(testResponse);

    }

    @Test
    void whenSetRequestBodyDetails_thenReturnValidObject() {
        ApiReqBody apiReqBody = imiConnect.setRequestBodyDetails(testPhoneNumber, testMessage);
        assertNotEquals(null, apiReqBody);
    }
}