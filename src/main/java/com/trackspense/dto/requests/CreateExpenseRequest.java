package com.trackspense.dto.requests;

import com.trackspense.data.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseRequest {
    @Min(value = 1, message = "Amount has to be greater than 0")
    private double amount;
    @NotNull(message = "Category is required")
    private Category category;
    @NotBlank(message = "Add Description")
    private String description;
    private LocalDateTime date;
}
