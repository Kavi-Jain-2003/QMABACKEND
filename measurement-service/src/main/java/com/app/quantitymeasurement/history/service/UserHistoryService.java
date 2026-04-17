package com.app.quantitymeasurement.history.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.quantitymeasurement.history.entity.UserHistory;
import com.app.quantitymeasurement.history.repository.UserHistoryRepository;

@Service
public class UserHistoryService {

    private final UserHistoryRepository repository;

    public UserHistoryService(UserHistoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveHistory(String type,
                            String input,
                            String output,
                            String status,
                            String username) {

        UserHistory history = new UserHistory();
        history.setOperationType(type);
        history.setInputData(input);
        history.setOutputData(output);
        history.setStatus(status);
        history.setUsername(username);
        history.setTimestamp(LocalDateTime.now());

        UserHistory saved = repository.saveAndFlush(history);
        System.out.println("HISTORY SAVED: " + type + " id=" + saved.getId());
    }

    @Transactional(readOnly = true)
    public List<UserHistory> getHistoryByUsername(String username) {
        return repository.findByUsernameOrderByTimestampDesc(username);
    }

    @Transactional
    public void deleteHistoryByUsername(String username) {
        repository.deleteByUsername(username);
    }
}
