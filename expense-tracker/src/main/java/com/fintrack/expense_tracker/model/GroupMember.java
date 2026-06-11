package com.fintrack.expense_tracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "group_members")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "net_balance", nullable = false)
    private Double netBalance = 0.0; // Positive: Owed to member, Negative: Member owes group

    public GroupMember() {}

    public GroupMember(String name, Double netBalance) {
        this.name = name;
        this.netBalance = netBalance;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getNetBalance() { return netBalance; }
    public void setNetBalance(Double netBalance) { this.netBalance = netBalance; }
}
