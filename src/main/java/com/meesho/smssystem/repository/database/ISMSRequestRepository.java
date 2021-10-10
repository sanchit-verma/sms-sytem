package com.meesho.smssystem.repository.database;

import com.meesho.smssystem.model.SMSRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISMSRequestRepository extends JpaRepository<SMSRecord,Long> {

}
