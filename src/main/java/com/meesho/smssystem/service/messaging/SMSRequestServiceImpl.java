package com.meesho.smssystem.service.messaging;

import com.meesho.smssystem.constants.Constants;
import com.meesho.smssystem.dtos.requests.smsRequests.SMSByPhoneNumberBetweenDatesRequest;
import com.meesho.smssystem.dtos.requests.smsRequests.SMSByTextRequest;
import com.meesho.smssystem.dtos.requests.smsRequests.SendSMSRequest;
import com.meesho.smssystem.dtos.response.smsresponse.SendSMSResponse;
import com.meesho.smssystem.dtos.response.smsresponse.getsmsbyidresponse.GetSMSByIdResponse;
import com.meesho.smssystem.dtos.response.smsresponse.getsmsbyidresponse.SMSDetailsAsResponse;
import com.meesho.smssystem.model.SMSRecord;
import com.meesho.smssystem.repository.cache.CacheBlacklistEntries;
import com.meesho.smssystem.repository.cache.CacheSMSEntries;
import com.meesho.smssystem.repository.database.IBlacklistReposiotry;
import com.meesho.smssystem.repository.database.ISMSRequestRepository;
import com.meesho.smssystem.repository.search_engine.SearchEngineRepository;
import com.meesho.smssystem.service.ProducerService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SMSRequestServiceImpl implements ISMSRequestService {

    private final ISMSRequestRepository ismsRequestRepository;
    private final IBlacklistReposiotry iBlacklistReposiotry;
    private final SearchEngineRepository searchEngineRepository;

    private final CacheBlacklistEntries cacheBlacklistEntries;
    private final CacheSMSEntries cacheSMSEntries;

    private final ProducerService producerService;

    private static final Logger logger = LogManager.getLogger(SMSRequestServiceImpl.class);

    @Autowired
    public SMSRequestServiceImpl(ISMSRequestRepository ismsRequestRepository , IBlacklistReposiotry iBlacklistReposiotry , CacheBlacklistEntries cacheBlacklistEntries, ProducerService producerService , CacheSMSEntries cacheSMSEntries , SearchEngineRepository searchEngineRepository) {
        this.ismsRequestRepository = ismsRequestRepository;
        this.iBlacklistReposiotry = iBlacklistReposiotry;
        this.cacheBlacklistEntries = cacheBlacklistEntries;
        this.producerService = producerService;
        this.cacheSMSEntries = cacheSMSEntries;
        this.searchEngineRepository = searchEngineRepository;
    }

    @Override
    public SendSMSResponse sendSMSProcess(SendSMSRequest smsRequest) {
        SendSMSResponse sendSMSResponse;
        SMSRecord smsRecord;
        boolean isBlacklisted = cacheBlacklistEntries.isPresentInCache(smsRequest.getPhone_number());
        if (!isBlacklisted) {
            if (iBlacklistReposiotry.getByPhoneNumber(smsRequest.getPhone_number()) != null) {
                cacheBlacklistEntries.insertToCache(smsRequest.getPhone_number());
                isBlacklisted = true;
            }
        }

        if (isBlacklisted) {
            smsRecord = getSmsRecordEntityAfterSave(smsRequest, Constants.MsgStatus.FAIL, Constants.FailureCode.NUMBER_BLACKLISTED);
            sendSMSResponse = getSendSMSResponseBySMSRecord(smsRecord, HttpStatus.FORBIDDEN);
        } else {
            logger.info(smsRequest.getPhone_number() + " is not blacklisted");
            smsRecord = getSmsRecordEntityAfterSave(smsRequest, Constants.MsgStatus.IN_QUEUE, null);
            logger.info("Putting Message request in Queue");
            sendSMSResponse = getSendSMSResponseBySMSRecord(smsRecord, HttpStatus.CREATED);
            producerService.sendRequest(smsRecord.getId());
        }
        return sendSMSResponse;

    }

    @Override
    public GetSMSByIdResponse getSMSById(String requestId) throws NotFoundException, NumberFormatException {
        Long id = Long.parseLong(requestId);
        SMSRecord smsRecord;
        if (cacheSMSEntries.isPresentInCache(id)) {
            smsRecord = cacheSMSEntries.getFromCache(id);
        } else {
            Optional<SMSRecord> optionalSMSRecord = ismsRequestRepository.findById(id);
            if(!optionalSMSRecord.isPresent()){
                throw new NotFoundException("No Records Found");
            }
            smsRecord = optionalSMSRecord.get();
            cacheSMSEntries.insertInCache(smsRecord);
        }

        SMSDetailsAsResponse smsDetailsAsResponse = SMSDetailsAsResponse.builder()
                .id(smsRecord.getId())
                .message(smsRecord.getMessage())
                .phoneNumber(smsRecord.getPhoneNumber())
                .status(smsRecord.getStatus())
                .cause(smsRecord.getFailureCode())
                .build();
        return GetSMSByIdResponse.builder()
                .data(smsDetailsAsResponse)
                .build();
    }

    @Override
    public String getSMSbyText(SMSByTextRequest smsByTextRequest) throws Exception {
        return searchEngineRepository.findByMessageContaining(smsByTextRequest.getText(), smsByTextRequest.getPage(), smsByTextRequest.getSize()).toString();
    }

    @Override
    public String getSMSbyPhoneNumberBetweenDates(SMSByPhoneNumberBetweenDatesRequest smsByPhoneNumberBetweenDatesRequest) throws Exception {
        return searchEngineRepository.findByPhoneNumberAndCreatedOnBetween(smsByPhoneNumberBetweenDatesRequest.getPhone_number()
                , smsByPhoneNumberBetweenDatesRequest.getFrom()
                , smsByPhoneNumberBetweenDatesRequest.getUpto()
                ,smsByPhoneNumberBetweenDatesRequest.getPage()
                , smsByPhoneNumberBetweenDatesRequest.getSize()).toString();
    }

    private SendSMSResponse getSendSMSResponseBySMSRecord(SMSRecord smsRecord, HttpStatus httpStatus) {
        SendSMSResponse sendSMSResponse;
        String failureCodeString;
        if(smsRecord.getFailureCode() == null){
            failureCodeString = "";
        }
        else{
            failureCodeString = " : " + smsRecord.getFailureCode().toString();
        }
        sendSMSResponse = SendSMSResponse.builder()
                .reqId(smsRecord.getId())
                .comments(smsRecord.getStatus().toString() + failureCodeString )
                .httpStatus(httpStatus)
                .build();
        return sendSMSResponse;
    }

    private SMSRecord getSmsRecordEntityAfterSave(SendSMSRequest smsRequest, Constants.MsgStatus msgStatus, Constants.FailureCode failureCode) {
        SMSRecord smsRecord = SMSRecord.builder()
                .phoneNumber(smsRequest.getPhone_number())
                .message(smsRequest.getMessage())
                .status(msgStatus)
                .failureCode(failureCode)
                .build();
        ismsRequestRepository.save(smsRecord);
        return smsRecord;
    }

}
