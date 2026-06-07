package com.fintrack.expense_tracker.controller;

import com.fintrack.expense_tracker.dto.ExtractedBillDTO;
import com.fintrack.expense_tracker.dto.ExpenseResponseDTO;
import com.fintrack.expense_tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // 📅 Week 3 Testing Endpoint
    @PostMapping("/upload")
    public ResponseEntity<ExpenseResponseDTO> uploadMockExpense(@RequestParam Long userId) {
        
        // 1. Mock data fetch kiya
        ExtractedBillDTO mockBill = expenseService.getMockBillData();
        
        // 2. Alert Engine Logic call kiya
        ExpenseResponseDTO response = expenseService.processExpenseAlerts(userId, mockBill);
        
        // 3. Response return kiya
        return ResponseEntity.ok(response);
    }
}