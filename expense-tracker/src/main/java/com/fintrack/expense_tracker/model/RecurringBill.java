package com.fintrack.expense_tracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "recurring_bills")
public class RecurringBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g. Netflix Premium, Room Rent

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false)
    private String category; // Entertainment, Bills, Rent

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private String status = "Active"; // Active, Paid

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public RecurringBill() {}

    public RecurringBill(String name, Double cost, String category, LocalDate dueDate, Long userId) {
        this.name = name;
        this.cost = cost;
        this.category = category;
        this.dueDate = dueDate;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
