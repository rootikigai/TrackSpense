package com.trackspense.services;

import com.trackspense.data.models.Category;
//import com.trackspense.data.models.Expense;
import com.trackspense.data.repos.ExpenseRepo;
import com.trackspense.dto.requests.CreateExpenseRequest;
import com.trackspense.dto.requests.UpdateExpenseRequest;
import com.trackspense.dto.responses.ExpenseResponse;
import com.trackspense.exceptions.ExpenseNotFoundException;
//import com.trackspense.mappers.ExpenseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class ExpenseServiceImplTest {

    private ExpenseServiceImpl expenseService;

    @Autowired
    private ExpenseRepo expenseRepo;

    private String userId;

    @BeforeEach
    void setUp() {
        expenseRepo.deleteAll();
        expenseService = new ExpenseServiceImpl(expenseRepo);
        userId = "test-user-123";
    }

    @Test
    @DisplayName("Add expense successfully")
    void testAddExpenseSuccess() {
        CreateExpenseRequest request = new CreateExpenseRequest(
                50.0,
                Category.FOOD,
                "Lunch",
                LocalDateTime.now()
        );

        ExpenseResponse response = expenseService.addExpense(request, userId);

        assertNotNull(response.getId());
        assertEquals(50.0, response.getAmount());
        assertEquals("FOOD", response.getCategory());
        assertEquals("Lunch", response.getDescription());
        assertEquals(userId, response.getUserId());
    }

    @Test
    @DisplayName("Add expense fails when userId is null")
    void testAddExpenseFailsWhenUserIdNull() {
        CreateExpenseRequest request = new CreateExpenseRequest(
                20.0,
                Category.TRANSPORT,
                "Bus ticket",
                LocalDateTime.now()
        );

        assertThrows(IllegalArgumentException.class, () -> expenseService.addExpense(request, null));
    }

    @Test
    @DisplayName("Add expense defaults date when null")
    void testAddExpenseDefaultsDate() {
        CreateExpenseRequest request = new CreateExpenseRequest(
                15.0,
                Category.ENTERTAINMENT,
                "Movie ticket",
                null
        );

        ExpenseResponse response = expenseService.addExpense(request, userId);

        assertNotNull(response.getDate());
    }

    @Test
    @DisplayName("Get all expenses by user")
    void testGetAllExpenses() {
        expenseService.addExpense(new CreateExpenseRequest(10.0, Category.FOOD, "Breakfast", LocalDateTime.now()), userId);
        expenseService.addExpense(new CreateExpenseRequest(20.0, Category.TRANSPORT, "Taxi", LocalDateTime.now()), userId);

        List<ExpenseResponse> expenses = expenseService.getAllExpenses(userId);

        assertEquals(2, expenses.size());
    }

    @Test
    @DisplayName("Update expense successfully")
    void testUpdateExpenseSuccess() {
        ExpenseResponse created = expenseService.addExpense(
                new CreateExpenseRequest(30.0, Category.HEALTH, "Medicine", LocalDateTime.now()),
                userId
        );

        UpdateExpenseRequest updateRequest = new UpdateExpenseRequest(
                35.0,
                Category.HEALTH,
                "Updated Medicine",
                LocalDateTime.now()
        );

        ExpenseResponse updated = expenseService.updateExpense(userId, created.getId(), updateRequest);

        assertEquals(35.0, updated.getAmount());
        assertEquals("Updated Medicine", updated.getDescription());
    }

    @Test
    @DisplayName("Update expense fails when not found")
    void testUpdateExpenseFailsWhenNotFound() {
        UpdateExpenseRequest request = new UpdateExpenseRequest(40.0, Category.FOOD, "Nonexistent", LocalDateTime.now());

        assertThrows(ExpenseNotFoundException.class, () -> expenseService.updateExpense(userId, "fake-id", request));
    }

    @Test
    @DisplayName("Delete expense successfully")
    void testDeleteExpenseSuccess() {
        ExpenseResponse created = expenseService.addExpense(
                new CreateExpenseRequest(100.0, Category.RENT, "Monthly rent", LocalDateTime.now()),
                userId
        );

        expenseService.deleteExpense(userId, created.getId());

        assertTrue(expenseRepo.findById(created.getId()).isEmpty());
    }

    @Test
    @DisplayName("Delete expense fails when not found")
    void testDeleteExpenseFailsWhenNotFound() {
        assertThrows(ExpenseNotFoundException.class, () -> expenseService.deleteExpense(userId, "bad-id"));
    }

    @Test
    @DisplayName("Get expenses by category")
    void testGetExpensesByUserAndCategory() {
        expenseService.addExpense(new CreateExpenseRequest(12.0, Category.FOOD, "Pizza", LocalDateTime.now()), userId);
        expenseService.addExpense(new CreateExpenseRequest(18.0, Category.ENTERTAINMENT, "Concert", LocalDateTime.now()), userId);

        List<ExpenseResponse> foodExpenses = expenseService.getExpensesByUserAndCategory(userId, Category.FOOD);

        assertEquals(1, foodExpenses.size());
        assertEquals("FOOD", foodExpenses.get(0).getCategory());
    }

    @Test
    @DisplayName("Get expenses by date range")
    void testGetExpensesByUserAndDateRange() {
        LocalDateTime now = LocalDateTime.now();
        expenseService.addExpense(new CreateExpenseRequest(10.0, Category.UTILITIES, "Water bill", now.minusDays(2)), userId);
        expenseService.addExpense(new CreateExpenseRequest(20.0, Category.UTILITIES, "Electric bill", now), userId);

        List<ExpenseResponse> results = expenseService.getExpensesByUserAndDateRange(userId, now.minusDays(3), now.plusDays(1));

        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Fails when userId is empty string")
    void testFailsWhenUserIdEmpty() {
        CreateExpenseRequest request = new CreateExpenseRequest(5.0, Category.OTHER, "Random", LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> expenseService.addExpense(request, "   "));
    }
}
