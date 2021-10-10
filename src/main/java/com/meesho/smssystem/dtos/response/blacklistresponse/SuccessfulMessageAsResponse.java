package com.meesho.smssystem.dtos.response.blacklistresponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessfulMessageAsResponse {
    private String data;
}
