package com.meesho.smssystem.service;

import com.meesho.smssystem.constants.Constants;
import com.meesho.smssystem.dtos.requests.apireq.ApiReqBody;
import com.meesho.smssystem.exceptions.IMIConnectException;
import com.meesho.smssystem.externalServices.MessagingAPI;
import com.meesho.smssystem.model.FullTextSearchRecord;
import com.meesho.smssystem.model.SMSRecord;
import com.meesho.smssystem.repository.database.ISMSRequestRepository;
import com.meesho.smssystem.repository.search_engine.SearchEngineRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ConsumerService {

    private final ISMSRequestRepository ismsRequestRepository;
    private final MessagingAPI messagingAPI;
    private final SearchEngineRepository searchEngineRepository;
    private SMSRecord smsRecord;

    private final String kafkaTopicName = "${spring.kafka.sms.topic-name}";
    private final String kafkaGroupId =  "${spring.kafka.consumer.group-id}";

    private static final Logger logger = LogManager.getLogger(ConsumerService.class);

    @Autowired
    public ConsumerService(ISMSRequestRepository ismsRequestRepository, MessagingAPI messagingAPI, SearchEngineRepository searchEngineRepository) {
        this.ismsRequestRepository = ismsRequestRepository;
        this.messagingAPI = messagingAPI;
        this.searchEngineRepository = searchEngineRepository;
    }

    @KafkaListener(topics = kafkaTopicName, groupId = kafkaGroupId)
    public void consume(Long requestId) {
        try{
            Optional<SMSRecord> optionalSMSRecord = ismsRequestRepository.findById(requestId);
            if(optionalSMSRecord.isPresent()){
                smsRecord = optionalSMSRecord.get();
                ApiReqBody apiReqBody = messagingAPI.setRequestBodyDetails(smsRecord.getPhoneNumber(), smsRecord.getMessage());
                messagingAPI.sendSMSRequestToAPI(apiReqBody);
                smsRecord.setStatus(Constants.MsgStatus.SENT);
                ismsRequestRepository.save(smsRecord);
                FullTextSearchRecord fullTextSearchRecord = getFullTextSearchEntity(smsRecord);
                searchEngineRepository.save(fullTextSearchRecord);
            }
        }
        catch (IMIConnectException exception){
            smsRecord.setStatus(Constants.MsgStatus.FAIL);
            smsRecord.setFailureCode(Constants.FailureCode.API_ERROR);
            ismsRequestRepository.save(smsRecord);
            logger.error("IMIConnect failed to send message : ", exception);
        }
        catch(Exception exception){
            logger.error(exception);
        }
    }

    private FullTextSearchRecord getFullTextSearchEntity(SMSRecord smsRecord) {
        return FullTextSearchRecord.builder()
                .Id(smsRecord.getId().toString())
                .createdOn(smsRecord.getCreatedOn())
                .phoneNumber(smsRecord.getPhoneNumber())
                .message(smsRecord.getMessage())
                .build();
    }
}
