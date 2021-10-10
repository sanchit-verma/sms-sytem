package com.meesho.smssystem.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullTextSearchRecord {

    @Id
    private String Id;

    private String phoneNumber;
    private String message;

    private Date createdOn;



}
