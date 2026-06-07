package com.fintrack.expense_tracker.service;

import com.fintrack.expense_tracker.dto.ExtractedBillDTO;
import com.fintrack.expense_tracker.dto.ExpenseResponseDTO;
import com.fintrack.expense_tracker.model.Budget;
import com.fintrack.expense_tracker.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    @Autowired
    private BudgetRepository budgetRepository;

    public ExtractedBillDTO getMockBillData() {
        ExtractedBillDTO mockBill = new ExtractedBillDTO();
        mockBill.setMerchant("Dominos Pizza");
        mockBill.setDate("2026-06-05");
        mockBill.setAmount(600.0);  
        mockBill.setTax(30.0);      
        mockBill.setCategory("Food"); 
        return mockBill;
    }

    public ExpenseResponseDTO processExpenseAlerts(Long userId, ExtractedBillDTO billDTO) {
        Budget budget = budgetRepository.findByUserIdAndCategory(userId, billDTO.getCategory())
                .orElse(new Budget(userId, billDTO.getCategory(), 5000.0, 4200.0));

        double projectedSpent = budget.getCurrentSpent() + billDTO.getAmount();
        boolean isOverBudget = false;
        String alertMessage = "Success: Expense processed within budget.";

        if (projectedSpent > budget.getMonthlyLimit()) {
            isOverBudget = true;
            alertMessage = "⚠️ Budget Exceeded! You are crossing your limit of ₹" + budget.getMonthlyLimit() + " for " + budget.getCategory() + ".";
        } else if (projectedSpent >= (budget.getMonthlyLimit() * 0.85)) {
            alertMessage = "⚠️ Warning: You have reached 85% of your budget limit for " + budget.getCategory() + ".";
        }

        budget.setCurrentSpent(projectedSpent);
        budgetRepository.save(budget);

        return new ExpenseResponseDTO(isOverBudget, alertMessage, projectedSpent);
    }
}