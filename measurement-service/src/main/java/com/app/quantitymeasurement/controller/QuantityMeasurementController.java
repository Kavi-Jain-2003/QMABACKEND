package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.history.entity.UserHistory;
import com.app.quantitymeasurement.history.service.UserHistoryService;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.QuantityModel;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.security.JwtUtil;
import com.app.quantitymeasurement.unit.Quantity;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/measurement")
public class QuantityMeasurementController {

    @Autowired
    private IQuantityMeasurementService service;

    @Autowired
    private UserHistoryService historyService;

    @Autowired
    private JwtUtil jwtUtil;

    // ================= HELPERS =================
    private Quantity<?> getQ1(QuantityMeasurementDTO input) {
        return QuantityModel.toQuantity(input.getThisQuantityDTO());
    }

    private Quantity<?> getQ2(QuantityMeasurementDTO input) {
        return QuantityModel.toQuantity(input.getThatQuantityDTO());
    }

    private String getUser(HttpServletRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String name = auth.getName();
            if (name != null && !name.isBlank() && !"anonymousUser".equalsIgnoreCase(name)) {
                return name;
            }
        }

        String header = request != null ? request.getHeader("Authorization") : null;
        if (header != null && header.startsWith("Bearer ")) {
            try {
                return jwtUtil.extractUsername(header.substring(7));
            } catch (Exception ignored) {
                // fall through
            }
        }

        return "anonymous";
    }

    private String formatQuantity(QuantityDTO dto) {
        if (dto == null) return "";
        return dto.getValue() + " " + dto.getUnit();
    }

    private String buildInput(String op, QuantityMeasurementDTO input) {
        String left = formatQuantity(input.getThisQuantityDTO());
        String right = formatQuantity(input.getThatQuantityDTO());

        return switch (op) {
            case "COMPARE" -> left + " vs " + right;
            case "ADD" -> left + " + " + right;
            case "SUBTRACT" -> left + " - " + right;
            case "MULTIPLY" -> left + " * " + right;
            case "DIVIDE" -> left + " / " + right;
            case "CONVERT" -> left + " to " + right;
            default -> left + " " + op + " " + right;
        };
    }

    private void storeHistory(String op, QuantityMeasurementDTO input, QuantityMeasurementEntity result, HttpServletRequest request) {
        if (result == null) return;

        String resultText = result.getError() != null && !result.getError().isBlank()
                ? result.getError()
                : String.valueOf(result.getResult());
        String status = result.getError() != null && !result.getError().isBlank()
                ? "FAILED"
                : "SUCCESS";

        try {
            historyService.saveHistory(op, buildInput(op, input), resultText, status, getUser(request));
        } catch (Exception e) {
            System.err.println("History save failed for " + op + ": " + e.getMessage());
        }
    }

    private QuantityMeasurementEntity execute(String op,
                                              QuantityMeasurementDTO input,
                                              Supplier<QuantityMeasurementEntity> action,
                                              HttpServletRequest request) {
        QuantityMeasurementEntity result;
        try {
            result = action.get();
        } catch (Exception e) {
            result = new QuantityMeasurementEntity(e.getMessage());
        }

        storeHistory(op, input, result, request);
        return result;
    }

    // ================= OPERATIONS =================

    @PostMapping("/compare")
    public QuantityMeasurementEntity compare(@RequestBody QuantityMeasurementDTO input, HttpServletRequest request) {
        return execute("COMPARE", input, () -> service.compare(getQ1(input), getQ2(input)), request);
    }

    @PostMapping("/add")
    public QuantityMeasurementEntity add(@RequestBody QuantityMeasurementDTO input, HttpServletRequest request) {
        return execute("ADD", input, () -> service.add(getQ1(input), getQ2(input)), request);
    }

    @PostMapping("/subtract")
    public QuantityMeasurementEntity subtract(@RequestBody QuantityMeasurementDTO input, HttpServletRequest request) {
        return execute("SUBTRACT", input, () -> service.subtract(getQ1(input), getQ2(input)), request);
    }

    @PostMapping("/divide")
    public QuantityMeasurementEntity divide(@RequestBody QuantityMeasurementDTO input, HttpServletRequest request) {
        return execute("DIVIDE", input, () -> service.divide(getQ1(input), getQ2(input)), request);
    }

    @PostMapping("/multiply")
    public QuantityMeasurementEntity multiply(@RequestBody QuantityMeasurementDTO input, HttpServletRequest request) {
        return execute("MULTIPLY", input, () -> service.multiply(getQ1(input), getQ2(input)), request);
    }

    @PostMapping("/convert")
    public QuantityMeasurementEntity convert(@RequestBody QuantityMeasurementDTO input, HttpServletRequest request) {
        return execute("CONVERT", input, () -> service.convert(getQ1(input), getQ2(input)), request);
    }

    // ================= HISTORY =================
    @GetMapping("/history")
    public List<UserHistory> getHistory(HttpServletRequest request) {
        return historyService.getHistoryByUsername(getUser(request));
    }

    @DeleteMapping("/history")
    public void clearHistory(HttpServletRequest request) {
        historyService.deleteHistoryByUsername(getUser(request));
    }

    public static class HistorySaveRequest {
        private String operationType;
        private String inputData;
        private String outputData;
        private String status;

        public String getOperationType() { return operationType; }
        public void setOperationType(String operationType) { this.operationType = operationType; }
        public String getInputData() { return inputData; }
        public void setInputData(String inputData) { this.inputData = inputData; }
        public String getOutputData() { return outputData; }
        public void setOutputData(String outputData) { this.outputData = outputData; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    @PostMapping("/history")
    public void saveHistory(@RequestBody HistorySaveRequest request, HttpServletRequest httpRequest) {
        historyService.saveHistory(
                request.getOperationType(),
                request.getInputData(),
                request.getOutputData(),
                request.getStatus(),
                getUser(httpRequest)
        );
    }

    // ================= TEST =================
    @GetMapping("/test")
    public String test() {
        return "Service is running!";
    }
}
