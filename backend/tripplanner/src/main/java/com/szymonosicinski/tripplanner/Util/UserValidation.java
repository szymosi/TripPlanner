package com.szymonosicinski.tripplanner.Util;

import com.szymonosicinski.tripplanner.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidation {
    @Autowired
    private UserRepository userRepository;

    public boolean validateUsername(String username){
        return !userRepository.existsByUsername(username);
    }

    public boolean validatePassword(String password){
        return password.length()>=8;
    }
}
