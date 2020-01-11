package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.DTO.UserDTO.LoginDTO;
import com.szymonosicinski.tripplanner.DTO.UserDTO.RegistrationDTO;
import com.szymonosicinski.tripplanner.DTO.UserDTO.UserDTO;
import com.szymonosicinski.tripplanner.Entity.User;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Exception.UserException;
import com.szymonosicinski.tripplanner.Repository.UserRepository;
import com.szymonosicinski.tripplanner.Util.JwtProvider;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import com.szymonosicinski.tripplanner.Util.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService{

    @Autowired
    UserValidation userValidation;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    public UserDTO register(RegistrationDTO registrationDto){
        if (!userValidation.validateUsername(registrationDto.getUsername()))
            throw new UserException(ExceptionMessage.USERNAME_TAKEN.toString());
        if (!userValidation.validatePassword(registrationDto.getPassword()))
            throw new UserException(ExceptionMessage.PASSWORD_NOT_VALID.toString());
        if(!registrationDto.getPassword().equals(registrationDto.getPasswordRepeat()))
            throw new UserException(ExceptionMessage.PASSWORDS_NOT_MATCH.toString());

        registrationDto.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        User user=modelMapper.map(registrationDto,User.class);
        userRepository.save(user);

        return modelMapper.map(user,UserDTO.class);
    }

    public String authenticate(final LoginDTO loginDTO){
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        return jwt;
    }

    public UserDTO getUser(String username){
        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UserException(ExceptionMessage.USER_NOT_EXIST.toString()));
        return modelMapper.map(user,UserDTO.class);
    }

    public UserDTO getUser(UUID id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new UserException(ExceptionMessage.USER_NOT_EXIST.toString()));
        return modelMapper.map(user,UserDTO.class);
    }

    public UserDTO getCurrentUser(UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.USER_NOT_LOGGED_IN.toString());
        UserDTO user=modelMapper.map(currentUser,UserDTO.class);
        return user;
    }
}
