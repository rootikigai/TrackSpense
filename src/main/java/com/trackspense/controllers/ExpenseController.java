package com.trackspense.controllers;

//import com.trackspense.data.models.Category;
import com.trackspense.dto.requests.CreateExpenseRequest;
import com.trackspense.dto.requests.UpdateExpenseRequest;
import com.trackspense.dto.responses.ExpenseResponse;
import com.trackspense.services.ExpenseServiceImpl;
import lombok.RequiredArgsConstructor;
//import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseServiceImpl expenseService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<ExpenseResponse> addExpense(@PathVariable String userId, @RequestBody CreateExpenseRequest request){
        ExpenseResponse savedExpense = expenseService.addExpense(request, userId);
        return ResponseEntity.ok(savedExpense);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable("id") String expenseId, @RequestBody UpdateExpenseRequest request){
        ExpenseResponse updatedExpense = expenseService.updateExpense(expenseId, request);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable("id") String id){
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Expense successfully deleted");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> list = expenseService.getAllExpenses();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUser(@PathVariable String userId) {
        List<ExpenseResponse> list = expenseService.getExpensesByUser(userId);
        return ResponseEntity.ok(list);
    }

//    @GetMapping("/user/{userId}/category/{category}")
//    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserAndCategory(
//            @PathVariable String userId,
//            @PathVariable String category) {
//        List<ExpenseResponse> list = expenseService.getExpensesByUserAndCategory(userId, category);
//        return ResponseEntity.ok(list);
//    }
//
//    @GetMapping("/user/{userId}/date")
//    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserAndDateRange(
//            @PathVariable String userId,
//            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
//            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
//
//        List<ExpenseResponse> list = expenseService.getExpensesByUserAndDateRange(userId, start, end);
//        return ResponseEntity.ok(list);
//    }
//
//    @GetMapping("/categories")
//    public ResponseEntity<List<String>> getAllCategories(){
//        List<String> categories = Arrays.stream(Category.values()).map(Enum::name).toList();
//        return ResponseEntity.ok(categories);
//    }
}
