package com.trackspense.controllers;

import com.trackspense.data.models.Category;
import com.trackspense.dto.requests.CreateExpenseRequest;
import com.trackspense.dto.requests.UpdateExpenseRequest;
import com.trackspense.dto.responses.ExpenseResponse;
import com.trackspense.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    private String getLoggedInUserId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping("/add")
    public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody CreateExpenseRequest request) {
        String userId = getLoggedInUserId();
        ExpenseResponse savedExpense = expenseService.addExpense(request, userId);
        return ResponseEntity.ok(savedExpense);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable("id") String expenseId,
            @Valid @RequestBody UpdateExpenseRequest request) {

        String userId = getLoggedInUserId();
        ExpenseResponse updatedExpense = expenseService.updateExpense(userId, expenseId, request);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable("id") String id) {
        String userId = getLoggedInUserId();
        expenseService.deleteExpense(userId, id);
        return ResponseEntity.ok("Expense successfully deleted");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        String userId = getLoggedInUserId();
        List<ExpenseResponse> list = expenseService.getAllExpenses(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/category/{category}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserAndCategory(@PathVariable Category category) {
        String userId = getLoggedInUserId();
        List<ExpenseResponse> list = expenseService.getExpensesByUserAndCategory(userId, category);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/date")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserAndDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        String userId = getLoggedInUserId();
        List<ExpenseResponse> list = expenseService.getExpensesByUserAndDateRange(userId, start, end);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = Arrays.stream(Category.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(categories);
    }
}
