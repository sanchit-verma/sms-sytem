package com.meesho.smssystem.service.blacklist;

import com.meesho.smssystem.dtos.requests.blacklistRequest.BlacklistBulkSave;
import com.meesho.smssystem.dtos.response.blacklistresponse.GetBlacklistedNumbersResponse;

import javax.validation.Valid;

public interface IBlacklistService {
    GetBlacklistedNumbersResponse getAllList();

    void InsertAllNumbers(@Valid BlacklistBulkSave inputList) throws Exception;

    void deleteByPhoneNumber(String phoneNumber);
}
