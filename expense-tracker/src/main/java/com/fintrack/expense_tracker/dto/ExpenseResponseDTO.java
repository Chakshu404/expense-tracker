package com.fintrack.expense_tracker.dto;

public class ExpenseResponseDTO {
    private boolean isOverBudget;
    private String alertMessage;
    private double projectedSpent;

    public ExpenseResponseDTO() {}

    public ExpenseResponseDTO(boolean isOverBudget, String alertMessage, double projectedSpent) {
        this.isOverBudget = isOverBudget;
        this.alertMessage = alertMessage;
        this.projectedSpent = projectedSpent;
    }

    public boolean isOverBudget() {
        return isOverBudget;
    }

    public void setOverBudget(boolean overBudget) {
        isOverBudget = overBudget;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public double getProjectedSpent() {
        return projectedSpent;
    }

    public void setProjectedSpent(double projectedSpent) {
        this.projectedSpent = projectedSpent;
    }
}
