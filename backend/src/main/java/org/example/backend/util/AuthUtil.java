package org.example.backend.util;

import org.example.backend.models.User;
import org.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;



@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;


    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return user;
        }
        else {
            throw new RuntimeException("User is not authenticated.");
        }
    }

    public String loggedInEmail() {
        return loggedInUser().getEmail();
    }

    public Long loggedInUserId() {
        return loggedInUser().getUserId();
    }
}