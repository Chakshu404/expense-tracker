package com.fintrack.expense_tracker.service;

import com.fintrack.expense_tracker.model.GroupExpense;
import com.fintrack.expense_tracker.model.GroupMember;
import com.fintrack.expense_tracker.repository.GroupExpenseRepository;
import com.fintrack.expense_tracker.repository.GroupMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class GroupSplitService {

    @Autowired
    private GroupExpenseRepository groupExpenseRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    public List<GroupMember> getAllMembers() {
        return groupMemberRepository.findAll();
    }

    public GroupMember addMember(String name) {
        if (groupMemberRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Friend member already exists: " + name);
        }
        return groupMemberRepository.save(new GroupMember(name, 0.0));
    }

    public List<GroupExpense> getSplitHistory() {
        return groupExpenseRepository.findAllByOrderByIdDesc();
    }

    @Transactional
    public GroupExpense splitBill(GroupExpense expense) {
        GroupExpense savedExpense = groupExpenseRepository.save(expense);

        Double amount = expense.getAmount();
        String payer = expense.getPaidBy();
        List<String> participants = expense.getSplits();
        
        int splitSize = participants.size();
        if (splitSize == 0) return savedExpense;

        Double share = amount / splitSize;

        if ("You".equalsIgnoreCase(payer)) {
            for (String participant : participants) {
                if ("You".equalsIgnoreCase(participant)) continue;

                GroupMember member = groupMemberRepository.findByName(participant)
                        .orElseGet(() -> groupMemberRepository.save(new GroupMember(participant, 0.0)));
                
                member.setNetBalance(member.getNetBalance() + share);
                groupMemberRepository.save(member);
            }
        } else {
            if (participants.contains("You")) {
                GroupMember member = groupMemberRepository.findByName(payer)
                        .orElseGet(() -> groupMemberRepository.save(new GroupMember(payer, 0.0)));
                
                member.setNetBalance(member.getNetBalance() - share);
                groupMemberRepository.save(member);
            }
        }

        return savedExpense;
    }

    @Transactional
    public void settleFriend(String name) {
        GroupMember member = groupMemberRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Group member not found: " + name));

        Double balance = member.getNetBalance();
        if (Math.abs(balance) < 0.01) return;

        String settlementDesc = balance > 0 
                ? String.format("Settlement: %s paid You", name)
                : String.format("Settlement: You paid %s", name);

        GroupExpense settlement = new GroupExpense(settlementDesc, Math.abs(balance), balance > 0 ? name : "You", List.of("You", name));
        groupExpenseRepository.save(settlement);

        member.setNetBalance(0.0);
        groupMemberRepository.save(member);
    }
}
