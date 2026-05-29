package org.example.backend.security.services;

import org.example.backend.models.User;
import org.example.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//fetch user details from the db, using username
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    //you tell Spring security: this is how you the user info in my app
    @Override
    @Transactional //the db operations are handled in a transaction
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username. " + username));

        return UserDetailsImpl.build(user); //bcs spring security expects in UserDetails type
    }
}
