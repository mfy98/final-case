package com.mfy98.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class CreateUserRequest {
    @NotBlank
    private String username;
    @NotBlank private String name;
    @Email
    private String email;
    @NotBlank  private String role;
}
