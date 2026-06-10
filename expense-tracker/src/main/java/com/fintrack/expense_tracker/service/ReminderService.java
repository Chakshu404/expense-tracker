package com.fintrack.expense_tracker.service;

import com.fintrack.expense_tracker.model.RecurringBill;
import com.fintrack.expense_tracker.repository.RecurringBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    @Autowired
    private RecurringBillRepository recurringBillRepository;

    public List<RecurringBill> getBillsByUserId(Long userId) {
        return recurringBillRepository.findByUserIdOrderByDueDateAsc(userId);
    }

    public RecurringBill saveSubscription(RecurringBill bill) {
        return recurringBillRepository.save(bill);
    }

    public void deleteSubscription(Long id) {
        recurringBillRepository.deleteById(id);
    }

    public List<RecurringBill> getUpcomingReminders(Long userId) {
        LocalDate today = LocalDate.now();
        List<RecurringBill> allBills = recurringBillRepository.findByUserIdOrderByDueDateAsc(userId);

        return allBills.stream()
                .filter(bill -> !"Paid".equalsIgnoreCase(bill.getStatus()))
                .filter(bill -> {
                    long daysUntilDue = ChronoUnit.DAYS.between(today, bill.getDueDate());
                    // Due in 2 days or overdue
                    return daysUntilDue >= 0 && daysUntilDue <= 2;
                })
                .collect(Collectors.toList());
    }

    public RecurringBill markAsPaid(Long billId) {
        RecurringBill bill = recurringBillRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Subscription not found with ID: " + billId));
        bill.setStatus("Paid");
        return recurringBillRepository.save(bill);
    }
}
