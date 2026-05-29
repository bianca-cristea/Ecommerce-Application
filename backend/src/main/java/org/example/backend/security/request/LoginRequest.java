package org.example.backend.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(min = 4, message = "Username should have more than 4 characters")
    private String username;

    @NotBlank
    @Size(min = 4, message = "Username should have more than 4 characters")
    private String password;

}
