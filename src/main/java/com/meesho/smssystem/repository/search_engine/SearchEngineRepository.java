package com.meesho.smssystem.repository.search_engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.meesho.smssystem.model.FullTextSearchRecord;

import java.util.Date;

public interface SearchEngineRepository {
    void save(FullTextSearchRecord doc) throws Exception;
    JsonNode findByMessageContaining(String text, int page, int size) throws Exception;
    JsonNode findByPhoneNumberAndCreatedOnBetween(String phoneNumber, Date from, Date upto, int page, int size) throws Exception;
}

