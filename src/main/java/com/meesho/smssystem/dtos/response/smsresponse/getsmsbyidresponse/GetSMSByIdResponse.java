package com.meesho.smssystem.dtos.response.smsresponse.getsmsbyidresponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSMSByIdResponse {
    SMSDetailsAsResponse data;
}
