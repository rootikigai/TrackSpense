package com.trackspense.services;

import com.trackspense.data.models.Expense;
import com.trackspense.data.repos.ExpenseRepo;
import com.trackspense.dto.requests.CreateExpenseRequest;
import com.trackspense.dto.requests.UpdateExpenseRequest;
import com.trackspense.dto.responses.ExpenseResponse;
import com.trackspense.exceptions.ExpenseNotFoundException;
import com.trackspense.mappers.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepo expenseRepo;

    @Override
    public ExpenseResponse addExpense(CreateExpenseRequest request, String userId) {
        // --- VALIDATIONS ---
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }

        // --- BUSINESS LOGIC ---
        Expense expense = ExpenseMapper.toEntity(request, userId);
        Expense savedExpense = expenseRepo.save(expense);
        return ExpenseMapper.toResponse(savedExpense);
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepo.findAll().stream().map(ExpenseMapper::toResponse).toList();
    }

    @Override
    public List<ExpenseResponse> getExpensesByUser(String userId) {
        return expenseRepo.findByUserId(userId).stream().map(ExpenseMapper::toResponse).toList();
    }

    @Override
    public ExpenseResponse updateExpense(String expenseId, UpdateExpenseRequest request) {
        Expense existing = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found: " + expenseId));

        // Validate fields (reuse validations from addExpense if needed)
        if (request.getAmount() <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (request.getCategory() == null || request.getCategory().trim().isEmpty())
            throw new IllegalArgumentException("Category is required");
        if (request.getDescription() == null || request.getDescription().trim().isEmpty())
            throw new IllegalArgumentException("Description cannot be blank");

        // Update entity via mapper
        ExpenseMapper.updateEntityFromDto(request, existing);

        Expense saved = expenseRepo.save(existing);
        return ExpenseMapper.toResponse(saved);
    }

    @Override
    public void deleteExpense(String expenseId) {
        Expense existing = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found: " + expenseId));
        expenseRepo.delete(existing);
    }
}
