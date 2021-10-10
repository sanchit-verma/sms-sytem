package com.meesho.smssystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
    private final KafkaTemplate<String,Long> kafkaTemplate;
    @Value("${spring.kafka.sms.topic-name}")
    private String topicName;

    @Autowired
    public ProducerService(KafkaTemplate<String, Long> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRequest(Long messageId){
        kafkaTemplate.send(topicName,messageId);
    }

}
