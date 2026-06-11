package com.fintrack.expense_tracker.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "group_expenses")
public class GroupExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "paid_by", nullable = false)
    private String paidBy; // e.g. "You" or a friend's name

    @ElementCollection
    @CollectionTable(name = "group_expense_splits", joinColumns = @JoinColumn(name = "group_expense_id"))
    @Column(name = "member_name")
    private List<String> splits; // List of member names splitting the bill

    public GroupExpense() {}

    public GroupExpense(String description, Double amount, String paidBy, List<String> splits) {
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getPaidBy() { return paidBy; }
    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }
    public List<String> getSplits() { return splits; }
    public void setSplits(List<String> splits) { this.splits = splits; }
}
