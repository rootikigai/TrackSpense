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
public class ExpenseService {
    private final ExpenseRepo expenseRepo;
    public ExpenseResponse addExpense(CreateExpenseRequest request, String loggedInUserId){
        Expense toSave = ExpenseMapper.toEntity(request, loggedInUserId);
        Expense saved = expenseRepo.save(toSave);
        return ExpenseMapper.toResponse(saved);
    }
    public ExpenseResponse updateExpense(String expenseId, UpdateExpenseRequest request){
        Expense existingExpense = expenseRepo.findById(expenseId).orElseThrow(() -> new ExpenseNotFoundException("Dem no fit find am: " + expenseId));
        ExpenseMapper.updateEntityFromDto(request, existingExpense);
        Expense saved = expenseRepo.save(existingExpense);

//        existingExpense.setAmount(updatedExpense.getAmount());
//        existingExpense.setCategory(updatedExpense.getCategory());
//        existingExpense.setDescription(updatedExpense.getDescription());
//        existingExpense.setDate(updatedExpense.getDate());
//        existingExpense.setUserId(updatedExpense.getUserId());

        return ExpenseMapper.toResponse(saved);
    }

    public void deleteExpense(String expenseId) {
        Expense existingExpense = expenseRepo.findById(expenseId).orElseThrow(() -> new ExpenseNotFoundException("Dem no fit find am: " + expenseId));
        expenseRepo.delete(existingExpense);
    }

    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepo.findAll().stream().map(ExpenseMapper::toResponse).toList();
    }

    public List<ExpenseResponse> getExpensesByUser(String userId){
        return expenseRepo.findByUserId(userId).stream().map(ExpenseMapper::toResponse).toList();
    }

    public List<ExpenseResponse> getExpensesByUserAndCategory(String userId, String category){
        return expenseRepo.findByUserIdAndCategory(userId, Category.valueOf(category)).stream().map(ExpenseMapper::toResponse).toList();
    }

    public List<ExpenseResponse> getExpensesByUserAndDateRange(String userId, LocalDateTime start, LocalDateTime end){
        return expenseRepo.findByUserIdAndDateBetween(userId, start, end).stream().map(ExpenseMapper::toResponse).toList();
    }
}
