package com.fintrack.expense_tracker.controller;

import com.fintrack.expense_tracker.model.RecurringBill;
import com.fintrack.expense_tracker.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@CrossOrigin(origins = "*")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @GetMapping
    public ResponseEntity<List<RecurringBill>> getSubscriptions(@RequestParam Long userId) {
        return ResponseEntity.ok(reminderService.getBillsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<RecurringBill> addSubscription(@RequestBody RecurringBill bill) {
        if (bill.getStatus() == null) {
            bill.setStatus("Active");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(reminderService.saveSubscription(bill));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        reminderService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<RecurringBill>> getUpcomingReminders(@RequestParam Long userId) {
        return ResponseEntity.ok(reminderService.getUpcomingReminders(userId));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<RecurringBill> paySubscription(@PathVariable Long id) {
        return ResponseEntity.ok(reminderService.markAsPaid(id));
    }
}
