package com.fintrack.expense_tracker.repository;

import com.fintrack.expense_tracker.model.RecurringBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecurringBillRepository extends JpaRepository<RecurringBill, Long> {
    List<RecurringBill> findByUserIdOrderByDueDateAsc(Long userId);
}
