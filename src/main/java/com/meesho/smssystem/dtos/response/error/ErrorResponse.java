package com.meesho.smssystem.dtos.response.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String error;
}
