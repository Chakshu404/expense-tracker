package com.fintrack.expense_tracker.dto;

public class ExtractedBillDTO {
    private String merchant;
    private String date;
    private double amount;
    private double tax;
    private String category;

    public ExtractedBillDTO() {}

    public ExtractedBillDTO(String merchant, String date, double amount, double tax, String category) {
        this.merchant = merchant;
        this.date = date;
        this.amount = amount;
        this.tax = tax;
        this.category = category;
    }

    public String getMerchant() { return merchant; }
    public void setMerchant(String merchant) { this.merchant = merchant; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}