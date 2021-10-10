package com.meesho.smssystem.model;

import com.meesho.smssystem.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SMSRecord implements Serializable {
    @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private String phoneNumber;

    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Constants.MsgStatus status;


    // This column will be null if status will be sent
    @Enumerated(EnumType.STRING)
    private Constants.FailureCode failureCode;


    @Column(nullable = false)
    @CreationTimestamp()
    private Date createdOn;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updatedOn;


}
