package com.trackspense.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExpenseRequest {
    private Double amount;
    private String category;
    private String description;
    private LocalDateTime date;
}
