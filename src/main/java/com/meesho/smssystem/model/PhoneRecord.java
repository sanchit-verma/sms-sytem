package com.meesho.smssystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneRecord implements Serializable{
    @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
    Long uniqueId;
    @Column(unique = true, nullable = false)
    String phoneNumber;
}
