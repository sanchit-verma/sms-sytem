package com.meesho.smssystem.dtos.requests.smsRequests;

import com.meesho.smssystem.constants.Constants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
public class SendSMSRequest {
    @NotNull
    @Pattern(regexp = Constants.regexpForPhoneNumber)
    private String phone_number;
    @NotNull
    private String message;
}
