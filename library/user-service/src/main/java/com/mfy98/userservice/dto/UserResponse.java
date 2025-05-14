package com.mfy98.userservice.dto;

import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long   id;
    private String username;
    private String name;
    private String email;
    private String role;

}