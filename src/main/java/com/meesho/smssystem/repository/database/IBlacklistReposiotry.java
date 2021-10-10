package com.meesho.smssystem.repository.database;

import com.meesho.smssystem.model.PhoneRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface IBlacklistReposiotry extends JpaRepository<PhoneRecord, Long> {
    List<PhoneRecord> findAll();

    PhoneRecord getByPhoneNumber(String inputPhoneNumber);

    @Transactional
    void deleteByPhoneNumber(String inputPhoneNumber);

}
