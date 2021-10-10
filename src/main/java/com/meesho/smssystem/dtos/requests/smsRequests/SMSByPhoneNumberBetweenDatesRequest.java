package com.meesho.smssystem.dtos.requests.smsRequests;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
public class SMSByPhoneNumberBetweenDatesRequest {
    @NotNull
    String phone_number;
    @NotNull
    Date from;
    @NotNull
    Date upto;
    @NotNull
    int page;
    @NotNull
    int size;
}
