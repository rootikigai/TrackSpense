package com.trackspense.dto.requests;

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
public class UpdateExpenseRequest {
    @Min(value = 1, message = "Amount must be greater than 0")
    private Double amount;
    @NotNull(message = "Category is required")
    private String category;
    @NotBlank(message = "Description can't be blank")
    private String description;
    private LocalDateTime date;
}
