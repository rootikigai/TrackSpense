package com.trackspense.data.repo;

import com.trackspense.data.model.Category;
import com.trackspense.data.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExpenseRepo extends MongoRepository<Expense,String> {
    List<Expense> findByUserId(String userId);
    List<Expense> findByCategory(Category category);
}
