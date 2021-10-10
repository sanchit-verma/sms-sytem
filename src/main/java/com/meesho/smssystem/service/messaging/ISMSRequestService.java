package com.meesho.smssystem.service.messaging;

import com.meesho.smssystem.dtos.requests.smsRequests.SendSMSRequest;
import com.meesho.smssystem.dtos.requests.smsRequests.SMSByPhoneNumberBetweenDatesRequest;
import com.meesho.smssystem.dtos.requests.smsRequests.SMSByTextRequest;
import com.meesho.smssystem.dtos.response.smsresponse.getsmsbyidresponse.GetSMSByIdResponse;
import com.meesho.smssystem.dtos.response.smsresponse.SendSMSResponse;
import javassist.NotFoundException;

public interface ISMSRequestService {

    // this method returns id of the updated status of message entity(Fail/Sent)
    SendSMSResponse sendSMSProcess(SendSMSRequest smsRequest) throws Exception;
    GetSMSByIdResponse getSMSById (String id) throws NotFoundException;
    String getSMSbyText (SMSByTextRequest smsByTextRequest) throws Exception;
    String getSMSbyPhoneNumberBetweenDates(SMSByPhoneNumberBetweenDatesRequest smsByPhoneNumberBetweenDatesRequest) throws Exception;
}
