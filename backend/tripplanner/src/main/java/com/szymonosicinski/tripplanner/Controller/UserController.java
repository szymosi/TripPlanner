package com.szymonosicinski.tripplanner.Controller;


import com.szymonosicinski.tripplanner.DTO.RegistrationDto;
import com.szymonosicinski.tripplanner.Repository.UserRepository;
import com.szymonosicinski.tripplanner.Service.UserService;
import org.hibernate.type.PostgresUUIDType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable("id") final UUID id){
        return new ResponseEntity(userRepository.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getUser(@RequestParam final String username){
        return new ResponseEntity(userRepository.findByUsername(username), HttpStatus.OK);
    }

    @PutMapping("/Registration")
    public ResponseEntity createUser(@RequestBody @Valid final RegistrationDto registrationDto){
        return new ResponseEntity(userService.register(registrationDto),HttpStatus.CREATED);
    }
}
