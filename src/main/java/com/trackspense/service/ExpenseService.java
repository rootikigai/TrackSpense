package com.trackspense.service;

import com.trackspense.data.model.Category;
import com.trackspense.dto.requests.CreateExpenseRequest;
import com.trackspense.dto.requests.UpdateExpenseRequest;
import com.trackspense.dto.responses.ExpenseResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseService {
    ExpenseResponse addExpense(CreateExpenseRequest request, String userId);

    ExpenseResponse updateExpense(String userId, String expenseId, UpdateExpenseRequest request);

    void deleteExpense(String userId, String expenseId);

    List<ExpenseResponse> getAllExpenses(String userId);

//    List<ExpenseResponse> getExpensesByUser(String userId);

    List<ExpenseResponse> getExpensesByUserAndCategory(String userId, Category category);

    List<ExpenseResponse> getExpensesByUserAndDateRange(String userId, LocalDateTime start, LocalDateTime end);
}
