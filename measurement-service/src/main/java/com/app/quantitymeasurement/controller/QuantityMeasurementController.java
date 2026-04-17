//package com.app.quantitymeasurement.controller;
//
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//
//import com.app.quantitymeasurement.history.entity.UserHistory;
//import com.app.quantitymeasurement.history.service.UserHistoryService;
//import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
//import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
//import com.app.quantitymeasurement.model.QuantityModel;
//import com.app.quantitymeasurement.unit.Quantity;
//import com.app.quantitymeasurement.service.IQuantityMeasurementService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/measurement")
//
//public class QuantityMeasurementController {
//
//	@Autowired
//	private IQuantityMeasurementService service;
//
//	@Autowired
//	private UserHistoryService historyService;
//
//	private Quantity<?> getQ1(QuantityMeasurementDTO input) {
//		return QuantityModel.toQuantity(input.getThisQuantityDTO());
//	}
//
//	private Quantity<?> getQ2(QuantityMeasurementDTO input) {
//		return QuantityModel.toQuantity(input.getThatQuantityDTO());
//	}
//
//	@PostMapping("/compare")
//	public QuantityMeasurementEntity compare(@RequestBody QuantityMeasurementDTO input) {
//
//	    QuantityMeasurementEntity result = service.compare(getQ1(input), getQ2(input));
//
//	    return result;
//	}
//	@PostMapping("/add")
//	public QuantityMeasurementEntity add(@RequestBody QuantityMeasurementDTO input) {
//
//	    QuantityMeasurementEntity result = service.add(getQ1(input), getQ2(input));
//
//	    return result;
//	}
//	@PostMapping("/subtract")
//	public QuantityMeasurementEntity subtract(@RequestBody QuantityMeasurementDTO input) {
//
//	    QuantityMeasurementEntity result = service.subtract(getQ1(input), getQ2(input));
//	    return result;
//	}
//	@PostMapping("/divide")
//	public QuantityMeasurementEntity divide(@RequestBody QuantityMeasurementDTO input) {
//
//	    QuantityMeasurementEntity result = service.divide(getQ1(input), getQ2(input));
//
//	    return result;
//	}
//	@PostMapping("/multiply")
//	public QuantityMeasurementEntity multiply(@RequestBody QuantityMeasurementDTO input) {
//
//	    QuantityMeasurementEntity result = service.multiply(getQ1(input), getQ2(input));
//
//	    return result;
//	}
//	@PostMapping("/convert")
//	public QuantityMeasurementEntity convert(@RequestBody QuantityMeasurementDTO input) {
//
//	    QuantityMeasurementEntity result = service.convert(getQ1(input), getQ2(input));
//
//
//	    return result;
//	}
//	@GetMapping("/history")
//	public List<UserHistory> getHistory() {
//		String username = SecurityContextHolder.getContext().getAuthentication().getName();
//		return historyService.getHistoryByUsername(username);
//	}
//
//	@GetMapping("/test")
//	public String test() {
//		return "Service is running!";
//	}
//}


package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.history.entity.UserHistory;
import com.app.quantitymeasurement.history.service.UserHistoryService;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.QuantityModel;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.unit.Quantity;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/measurement")
public class QuantityMeasurementController {

    @Autowired
    private IQuantityMeasurementService service;

    @Autowired
    private UserHistoryService historyService;

    // ================= HELPERS =================
    private Quantity<?> getQ1(QuantityMeasurementDTO input) {
        return QuantityModel.toQuantity(input.getThisQuantityDTO());
    }

    private Quantity<?> getQ2(QuantityMeasurementDTO input) {
        return QuantityModel.toQuantity(input.getThatQuantityDTO());
    }

    private String getUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "anonymous";
    }

    private void saveHistorySafely(String type,
                                   String input,
                                   String output,
                                   String status) {
        try {
            historyService.saveHistory(type, input, output, status, getUser());
        } catch (Exception e) {
            System.err.println("History save failed for " + type + ": " + e.getMessage());
        }
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

    private void storeHistory(String op, QuantityMeasurementDTO input, QuantityMeasurementEntity result) {
        if (result == null) return;

        String resultText = result.getError() != null && !result.getError().isBlank()
                ? result.getError()
                : String.valueOf(result.getResult());
        String status = result.getError() != null && !result.getError().isBlank()
                ? "FAILED"
                : "SUCCESS";

        saveHistorySafely(op, buildInput(op, input), resultText, status);
    }

    private QuantityMeasurementEntity execute(String op,
                                              QuantityMeasurementDTO input,
                                              Supplier<QuantityMeasurementEntity> action) {
        QuantityMeasurementEntity result;
        try {
            result = action.get();
        } catch (Exception e) {
            result = new QuantityMeasurementEntity(e.getMessage());
        }

        storeHistory(op, input, result);
        return result;
    }

    // ================= OPERATIONS =================

    @PostMapping("/compare")
    public QuantityMeasurementEntity compare(@RequestBody QuantityMeasurementDTO input) {
        return execute("COMPARE", input, () -> service.compare(getQ1(input), getQ2(input)));
    }

    @PostMapping("/add")
    public QuantityMeasurementEntity add(@RequestBody QuantityMeasurementDTO input) {
        return execute("ADD", input, () -> service.add(getQ1(input), getQ2(input)));
    }

    @PostMapping("/subtract")
    public QuantityMeasurementEntity subtract(@RequestBody QuantityMeasurementDTO input) {
        return execute("SUBTRACT", input, () -> service.subtract(getQ1(input), getQ2(input)));
    }

    @PostMapping("/divide")
    public QuantityMeasurementEntity divide(@RequestBody QuantityMeasurementDTO input) {
        return execute("DIVIDE", input, () -> service.divide(getQ1(input), getQ2(input)));
    }

    @PostMapping("/multiply")
    public QuantityMeasurementEntity multiply(@RequestBody QuantityMeasurementDTO input) {
        return execute("MULTIPLY", input, () -> service.multiply(getQ1(input), getQ2(input)));
    }

    @PostMapping("/convert")
    public QuantityMeasurementEntity convert(@RequestBody QuantityMeasurementDTO input) {
        return execute("CONVERT", input, () -> service.convert(getQ1(input), getQ2(input)));
    }

    // ================= HISTORY =================
    @GetMapping("/history")
    public List<UserHistory> getHistory() {
        return historyService.getHistoryByUsername(getUser());
    }

    @DeleteMapping("/history")
    public void clearHistory() {
        historyService.deleteHistoryByUsername(getUser());
    }

    // ================= TEST =================
    @GetMapping("/test")
    public String test() {
        return "Service is running!";
    }
}
