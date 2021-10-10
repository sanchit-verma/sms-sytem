package com.meesho.smssystem.dtos.response.smsresponse.getsmsbyidresponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meesho.smssystem.constants.Constants;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SMSDetailsAsResponse {
    private Long id;
    private String phoneNumber;
    private String message;
    private Constants.MsgStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Constants.FailureCode cause;
}
