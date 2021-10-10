package com.meesho.smssystem.dtos.response.smsresponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendSMSResponse {
    private Long reqId;
    private String comments;
    @JsonIgnore
    HttpStatus httpStatus;
}
