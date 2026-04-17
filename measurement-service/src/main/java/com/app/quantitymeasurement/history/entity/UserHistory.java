package com.app.quantitymeasurement.history.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_history")
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operationType;

    @Column(columnDefinition = "TEXT")
    private String inputData;

    @Column(columnDefinition = "TEXT")
    private String outputData;

    private String status;

    @Column(name = "history_timestamp")
    private LocalDateTime historyTimestamp;

    private String username;

    public UserHistory() {}

    public UserHistory(String operationType, String inputData,
                       String outputData, String status, String username) {
        this.operationType = operationType;
        this.inputData = inputData;
        this.outputData = outputData;
        this.status = status;
        this.username = username;
        this.historyTimestamp = LocalDateTime.now();
    }

    // getters + setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }

    public String getInputData() { return inputData; }
    public void setInputData(String inputData) { this.inputData = inputData; }

    public String getOutputData() { return outputData; }
    public void setOutputData(String outputData) { this.outputData = outputData; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getHistoryTimestamp() { return historyTimestamp; }
    public void setHistoryTimestamp(LocalDateTime historyTimestamp) { this.historyTimestamp = historyTimestamp; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
