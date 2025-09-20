package com.trackspense.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "expenses")

public class Expense {
    @Id
    private String id;
    private String description;
    private double amount;
    private Category category;
    private LocalDateTime date;
    private String userId;
}
