package com.trackspense.services;

import com.trackspense.data.models.Category;
import com.trackspense.data.models.Expense;
import com.trackspense.data.repos.ExpenseRepo;
import com.trackspense.dto.requests.CreateExpenseRequest;
import com.trackspense.dto.requests.UpdateExpenseRequest;
import com.trackspense.dto.responses.ExpenseResponse;
import com.trackspense.exceptions.ExpenseNotFoundException;
import com.trackspense.mappers.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepo expenseRepo;

    @Override
    public ExpenseResponse addExpense(CreateExpenseRequest request, String userId) {
        validateUserId(userId);

        Expense expense = ExpenseMapper.toEntity(request, userId);
        if (expense.getDate() == null) {
            expense.setDate(LocalDateTime.now());
        }

        Expense savedExpense = expenseRepo.save(expense);
        return ExpenseMapper.toResponse(savedExpense);
    }

    @Override
    public List<ExpenseResponse> getAllExpenses(String userId) {
        validateUserId(userId);

        return expenseRepo.findByUserId(userId)
                .stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    @Override
    public List<ExpenseResponse> getExpensesByUser(String userId) {
        validateUserId(userId);

        return expenseRepo.findByUserId(userId)
                .stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    @Override
    public ExpenseResponse updateExpense(String userId, String expenseId, UpdateExpenseRequest request) {
        validateUserId(userId);

        Expense existing = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found: " + expenseId));

        if (!existing.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this expense");
        }

        ExpenseMapper.updateEntityFromDto(request, existing);
        Expense saved = expenseRepo.save(existing);

        return ExpenseMapper.toResponse(saved);
    }

    @Override
    public void deleteExpense(String userId, String expenseId) {
        validateUserId(userId);

        Expense existing = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found: " + expenseId));

        if (!existing.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }

        expenseRepo.delete(existing);
    }

    @Override
    public List<ExpenseResponse> getExpensesByUserAndCategory(String userId, Category category) {
        validateUserId(userId);

        return expenseRepo.findByUserIdAndCategory(userId, category)
                .stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    @Override
    public List<ExpenseResponse> getExpensesByUserAndDateRange(String userId, LocalDateTime start, LocalDateTime end) {
        validateUserId(userId);

        return expenseRepo.findByUserIdAndDateBetween(userId, start, end)
                .stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    // ---------------- PRIVATE HELPERS ----------------

    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
    }
}
