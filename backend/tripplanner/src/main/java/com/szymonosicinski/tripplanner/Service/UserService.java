package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.DTO.RegistrationDto;
import com.szymonosicinski.tripplanner.Entity.User;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Exception.UserException;
import com.szymonosicinski.tripplanner.Repository.UserRepository;
import com.szymonosicinski.tripplanner.Util.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    @Autowired UserValidation userValidation;
    @Autowired ModelMapper modelMapper;
    @Autowired UserRepository userRepository;

    public String register(RegistrationDto registrationDto){
        if (!userValidation.validateUsername(registrationDto.getSurname()))
            throw new UserException(ExceptionMessage.USERNAME_TAKEN.toString());
        if (!userValidation.validatePassword(registrationDto.getPassword()))
            throw new UserException(ExceptionMessage.PASSWORD_NOT_VALID.toString());
        if(!registrationDto.getPassword().equals(registrationDto.getPasswordRepeat()))
            throw new UserException(ExceptionMessage.PASSWORDS_NOT_MATCH.toString());
        User user=modelMapper.map(registrationDto,User.class);
        userRepository.save(user);

        return registrationDto.getUsername();
    }

}
