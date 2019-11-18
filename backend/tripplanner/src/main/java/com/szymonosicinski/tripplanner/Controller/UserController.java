package com.szymonosicinski.tripplanner.Controller;


import com.szymonosicinski.tripplanner.DTO.User.LoginDTO;
import com.szymonosicinski.tripplanner.DTO.User.RegistrationDto;
import com.szymonosicinski.tripplanner.DTO.User.UserDTO;
import com.szymonosicinski.tripplanner.Service.UserService;
import com.szymonosicinski.tripplanner.Util.CurrentUser;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
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
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable("id") final UUID id){
        return new ResponseEntity(userService.getUser(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getUser(@RequestParam(value="username") final String username){
        return new ResponseEntity<UserDTO>(userService.getUser(username), HttpStatus.OK);
    }

    @GetMapping("/CurrentUser")
    public ResponseEntity getCurrentUser(@CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(userService.getCurrentUser(currentUser),HttpStatus.OK);
    }

    @PutMapping("/Registration")
    public ResponseEntity createUser(@RequestBody @Valid final RegistrationDto registrationDto){
        return new ResponseEntity(userService.register(registrationDto),HttpStatus.CREATED);
    }

    @PostMapping("/Login")
    public ResponseEntity login(@RequestBody final LoginDTO loginDTO){
        return userService.authenticate(loginDTO);
    }
}
