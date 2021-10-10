package com.meesho.smssystem.service.blacklist;

import com.meesho.smssystem.dtos.requests.blacklistRequest.BlacklistBulkSave;
import com.meesho.smssystem.dtos.response.blacklistresponse.GetBlacklistedNumbersResponse;
import com.meesho.smssystem.model.PhoneRecord;
import com.meesho.smssystem.repository.cache.CacheBlacklistEntries;
import com.meesho.smssystem.repository.database.IBlacklistReposiotry;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Service
public class BlacklistServiceImpl implements IBlacklistService {

    private final CacheBlacklistEntries cacheBlacklistEntries;
    private final IBlacklistReposiotry blacklistRepository;

    @Autowired
    public BlacklistServiceImpl(IBlacklistReposiotry blacklistRepository, CacheBlacklistEntries cacheBlacklistEntries) {
        this.blacklistRepository = blacklistRepository;
        this.cacheBlacklistEntries = cacheBlacklistEntries;
    }

    @Override
    public GetBlacklistedNumbersResponse getAllList() {
        List<PhoneRecord> recordList = blacklistRepository.findAll();
        List<String> phoneNumberList = new ArrayList<>();
        for (PhoneRecord phoneRecord : recordList) {
            String phoneNumber = phoneRecord.getPhoneNumber();
            phoneNumberList
                    .add(phoneNumber);
        }
        return GetBlacklistedNumbersResponse.builder()
                .data(phoneNumberList)
                .build();
    }

    @Override
    public void InsertAllNumbers(@Valid BlacklistBulkSave blacklistBulkSave) throws Exception {
        List<PhoneRecord> recordList = new ArrayList<>();
        if(blacklistBulkSave.getPhone_numbers().isEmpty())
            throw new NotFoundException("No Valid Entries Found");
        for (String phoneNumberInList : blacklistBulkSave.getPhone_numbers()) {
            PhoneRecord phoneRecord = PhoneRecord.builder()
                    .phoneNumber(phoneNumberInList)
                    .build();
            recordList.add(phoneRecord);
        }
        blacklistRepository.saveAll(recordList);
    }

    @Override
    public void deleteByPhoneNumber(String phoneNumber) {
        cacheBlacklistEntries.deleteCacheEntry(phoneNumber);
        blacklistRepository.deleteByPhoneNumber(phoneNumber);
    }
}
