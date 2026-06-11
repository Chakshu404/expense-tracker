package com.fintrack.expense_tracker.repository;

import com.fintrack.expense_tracker.model.GroupExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupExpenseRepository extends JpaRepository<GroupExpense, Long> {
    List<GroupExpense> findAllByOrderByIdDesc();
}
