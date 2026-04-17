package com.app.quantitymeasurement.history.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.app.quantitymeasurement.history.entity.UserHistory;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    List<UserHistory> findByUsernameOrderByHistoryTimestampDesc(String username);

    @Transactional
    @Modifying
    @Query("delete from UserHistory h where h.username = :username")
    void deleteByUsername(@Param("username") String username);
}
