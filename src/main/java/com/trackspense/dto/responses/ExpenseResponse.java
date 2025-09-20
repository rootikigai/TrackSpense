package com.trackspense.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private String id;
    private double amount;
    private String category;
    private String description;
    private LocalDateTime date;
    private String userId;
}
