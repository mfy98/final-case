package com.mfy98.borrowservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBorrowingRequest {
    @NotNull private Long bookId;
    @NotNull private Long userId;
}