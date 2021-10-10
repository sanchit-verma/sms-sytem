package com.meesho.smssystem.dtos.response.blacklistresponse;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetBlacklistedNumbersResponse {
    private List<String> data;
}
