package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.Entity.User;
import com.szymonosicinski.tripplanner.Repository.UserRepository;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException{
        final User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User " + username + " does not exist"));
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(final UUID id) throws UsernameNotFoundException{
        final User user=userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("User " + id + " does not exist"));
        return UserPrincipal.create(user);
    }
}
