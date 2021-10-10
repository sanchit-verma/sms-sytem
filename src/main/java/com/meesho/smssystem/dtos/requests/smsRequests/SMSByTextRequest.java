package com.meesho.smssystem.dtos.requests.smsRequests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SMSByTextRequest {

    @NotNull
    String text;
    @NotNull
    int page;
    @NotNull
    int size;
}
