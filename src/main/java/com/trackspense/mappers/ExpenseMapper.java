package com.trackspense.mappers;

import com.trackspense.data.models.Category;
import com.trackspense.data.models.Expense;
import com.trackspense.dto.requests.CreateExpenseRequest;
import com.trackspense.dto.requests.UpdateExpenseRequest;
import com.trackspense.dto.responses.ExpenseResponse;

public class ExpenseMapper {
    public static Expense toEntity(CreateExpenseRequest request, String userId){
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        if (request.getCategory() != null) {
            expense.setCategory(Category.valueOf(request.getCategory().toUpperCase()));
        }
        expense.setDate(request.getDate() != null ? request.getDate() : java.time.LocalDateTime.now());
        expense.setUserId(userId);
        return expense;
    }

    public static void updateEntityFromDto(UpdateExpenseRequest request, Expense existing) {
        existing.setDescription(request.getDescription());
        existing.setAmount(request.getAmount());
        if (request.getCategory() != null) {
            existing.setCategory(Category.valueOf(request.getCategory().toUpperCase()));
        }
        existing.setDate(request.getDate() != null ? request.getDate() : existing.getDate());
    }

    public static ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(expense.getId(), expense.getAmount(), expense.getCategory() != null ? expense.getCategory().name() : null, expense.getDescription(), expense.getDate());
    }
}
