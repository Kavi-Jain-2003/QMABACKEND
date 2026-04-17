package com.app.quantitymeasurement.history.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.quantitymeasurement.history.entity.UserHistory;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    List<UserHistory> findByUsernameOrderByTimestampDesc(String username);

    void deleteByUsername(String username);
}
