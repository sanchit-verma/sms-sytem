package com.meesho.smssystem.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
    @Value("${elasticsearch.host.port}")
    private int elasticSearchPort;
    @Value("${elasticsearch.host.name}")
    private String elasticsearchHostName;
    @Value("${elasticsearch.host.scheme}")
    private String scheme;
    @Value("${elasticsearch.authenticate.username}")
    private String userName;
    @Value("${elasticsearch.authenticate.password}")
    private String password;

    @Bean(destroyMethod = "close")
    public RestClient client(){
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY , new UsernamePasswordCredentials(userName , password));
        return  RestClient.builder(new HttpHost(elasticsearchHostName, elasticSearchPort, scheme))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                .build();
    }
}
