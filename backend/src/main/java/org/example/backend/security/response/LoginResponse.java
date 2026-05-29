package org.example.backend.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LoginResponse {

    private Long id;
    private String username;
    private List<String> roles;
    //private String jwtToken; // so that client can use it in next requests


    public LoginResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
