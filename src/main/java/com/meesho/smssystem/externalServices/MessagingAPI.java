package com.meesho.smssystem.externalServices;

import com.meesho.smssystem.dtos.requests.apireq.ApiReqBody;

public interface MessagingAPI {
    void sendSMSRequestToAPI(ApiReqBody apiReqBody) throws Exception;

    ApiReqBody setRequestBodyDetails(String phoneNumber, String message);
}
