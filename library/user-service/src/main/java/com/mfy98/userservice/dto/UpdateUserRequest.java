package com.mfy98.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateUserRequest {
    @NotBlank private String name;
    @Email    private String email;
    @NotBlank private String role;
}
