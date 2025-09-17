package com.trackspense.data.repos;

import com.trackspense.data.models.Category;
import com.trackspense.data.models.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepo extends MongoRepository<Expense,String> {
    List<Expense> findByUserId(String userId);
    List<Expense> findByUserIdAndCategory(String userId, Category category);
    List<Expense> findByUserIdAndDateBetween(String userId, LocalDateTime start, LocalDateTime end);
}
