package com.fintrack.expense_tracker.controller;

import com.fintrack.expense_tracker.model.GroupExpense;
import com.fintrack.expense_tracker.model.GroupMember;
import com.fintrack.expense_tracker.service.GroupSplitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/split")
@CrossOrigin(origins = "*")
public class GroupSplitController {

    @Autowired
    private GroupSplitService groupSplitService;

    @GetMapping("/members")
    public ResponseEntity<List<GroupMember>> getGroupMembers() {
        return ResponseEntity.ok(groupSplitService.getAllMembers());
    }

    @PostMapping("/members")
    public ResponseEntity<GroupMember> addGroupMember(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(groupSplitService.addMember(name));
    }

    @GetMapping("/history")
    public ResponseEntity<List<GroupExpense>> getSplitHistory() {
        return ResponseEntity.ok(groupSplitService.getSplitHistory());
    }

    @PostMapping("/bill")
    public ResponseEntity<GroupExpense> splitBill(@RequestBody GroupExpense expense) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupSplitService.splitBill(expense));
    }

    @PostMapping("/settle")
    public ResponseEntity<Void> settleFriend(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        groupSplitService.settleFriend(name);
        return ResponseEntity.ok().build();
    }
}
