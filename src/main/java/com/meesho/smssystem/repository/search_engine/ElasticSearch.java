package com.meesho.smssystem.repository.search_engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.smssystem.constants.Constants;
import com.meesho.smssystem.exceptions.ElasticSearchException;
import com.meesho.smssystem.model.FullTextSearchRecord;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class ElasticSearch implements SearchEngineRepository {

    private final ObjectMapper objectMapper;
    private final RestClient restClient;
    @Value("${elasticsearch.index-name}")
    private String indexName;

    @Autowired
    public ElasticSearch(@Qualifier("pojoMapper") ObjectMapper objectMapper, RestClient restClient) {
        this.objectMapper = objectMapper;
        this.restClient = restClient;
    }
    @Override
    public void save(FullTextSearchRecord doc) throws ElasticSearchException, IOException {
        //JsonProcessingException
        String api = indexName + "/_doc/" + doc.getId();
        Request request = new Request("POST",api);
        String docAsJson = objectMapper.writeValueAsString(doc);
        request.setJsonEntity(docAsJson);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        if(!jsonNode.get("result").asText().equals("created")){
            throw new ElasticSearchException("Elastic Search Save Failed");
        }
    }
    @Override
    public JsonNode findByMessageContaining(String text, int page, int size) throws IOException, ElasticSearchException {
        String api = indexName + "/_search";
        Request request = new Request("POST",api);
        String getSMSByTextQueryAsString = generateSmsQuery(text,page,size).toString();
        request.setJsonEntity(getSMSByTextQueryAsString);
        return processSearchRequest(request);
    }
    @Override
    public JsonNode findByPhoneNumberAndCreatedOnBetween(String phoneNumber, Date from, Date upto, int page, int size) throws IOException, ElasticSearchException {
        String api = indexName + "/_search";
        Request request = new Request("POST",api);
        String getByPhoneNumberAndDateBetweenQueryString = generateRangeQuery(phoneNumber,from,upto,page,size).toString();
        request.setJsonEntity(getByPhoneNumberAndDateBetweenQueryString);
        return processSearchRequest(request);
    }

    private JsonNode processSearchRequest(Request request) throws ElasticSearchException, IOException {
        Response response = restClient.performRequest(request);
        if(response == null){
            throw new ElasticSearchException("ElasticSearch returned invalid response!");
        }
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("hits");
    }

    private JSONObject generateSmsQuery(String text, int page, int size) throws JSONException {
        int offset = page * size;
        // return "";
        JSONArray fieldList = new JSONArray();
        fieldList.put("message");
        JSONObject queryStringObj = new JSONObject();
        queryStringObj.put("query","*"+text+"*");
        queryStringObj.put("fields",fieldList);
        JSONObject queryString = new JSONObject();
        queryString.put("query_string",queryStringObj);
        JSONArray mustArray = new JSONArray();
        mustArray.put(queryString);
        JSONObject must = new JSONObject();
        must.put("must",mustArray);
        JSONObject boolObject = new JSONObject();
        boolObject.put("bool",must);
        JSONObject query = new JSONObject();
        query.put("from",offset);
        query.put("size",size);
        query.put("query",boolObject);
        return query;
    }
    private JSONObject generateRangeQuery(String phoneNumber, Date from, Date upto, int page, int size) throws JSONException {
        int offset = page * size;
        DateFormat dateFormat = new SimpleDateFormat(Constants.datePattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String fromAsString = dateFormat.format(from);
        String uptoAsString = dateFormat.format(upto);
        JSONObject phObject = new JSONObject();
        JSONObject match = new JSONObject();
        JSONObject gteLte = new JSONObject();
        JSONObject createdOn = new JSONObject();
        JSONObject range = new JSONObject();
        JSONArray mustArray = new JSONArray();
        JSONObject must = new JSONObject();
        JSONObject bool = new JSONObject();
        JSONObject query = new JSONObject();
        phObject.put("phoneNumber",phoneNumber);
        match.put("match",phObject);
        gteLte.put("gte", fromAsString);
        gteLte.put("lte", uptoAsString);
        createdOn.put("createdOn",gteLte);
        range.put("range",createdOn);
        mustArray.put(match);
        mustArray.put(range);
        must.put("must", mustArray);
        bool.put("bool",must);
        query.put("query",bool);
        query.put("from",offset);
        query.put("size",size);
        return query;
    }

    }
