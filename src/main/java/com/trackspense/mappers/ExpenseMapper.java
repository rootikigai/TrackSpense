package com.trackspense.mappers;

import com.trackspense.data.models.Expense;
import com.trackspense.dto.requests.CreateExpenseRequest;
import com.trackspense.dto.requests.UpdateExpenseRequest;
import com.trackspense.dto.responses.ExpenseResponse;

import java.time.LocalDateTime;

public class ExpenseMapper {

    public static Expense toEntity(CreateExpenseRequest request, String userId) {
        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate() != null ? request.getDate() : LocalDateTime.now());
        expense.setUserId(userId);
        return expense;
    }

    public static ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getAmount(),
                expense.getCategory() != null ? expense.getCategory().name() : null,
                expense.getDescription(),
                expense.getDate(),
                expense.getUserId()
        );
    }

    public static void updateEntityFromDto(UpdateExpenseRequest request, Expense existing) {
        existing.setAmount(request.getAmount());
        existing.setDescription(request.getDescription());
        existing.setCategory(request.getCategory());
        existing.setDate(request.getDate() != null ? request.getDate() : existing.getDate());
    }
}
