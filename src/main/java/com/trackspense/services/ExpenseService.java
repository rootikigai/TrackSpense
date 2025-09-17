package com.trackspense.services;

import com.trackspense.data.models.Expense;
import com.trackspense.data.repos.ExpenseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepo expenseRepo;
    public Expense addExpense(Expense expense){
        return expenseRepo.save(expense);
    }
    public Expense updateExpense(String expenseId, Expense updatedExpense){
        Expense existingExpense = expenseRepo.findById(expenseId).orElseThrow(() -> new RuntimeException("Dem no fit find am!"));

        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDescription(updatedExpense.getDescription());
        existingExpense.setDate(updatedExpense.getDate());
        existingExpense.setUserId(updatedExpense.getUserId());

        return expenseRepo.save(existingExpense);
    }
}
