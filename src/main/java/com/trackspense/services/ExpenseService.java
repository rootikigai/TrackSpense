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
}
