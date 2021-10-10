package com.meesho.smssystem.dtos.requests.smsRequests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetSMSByIdRequest {
    @NotNull
    String request_id;
}
