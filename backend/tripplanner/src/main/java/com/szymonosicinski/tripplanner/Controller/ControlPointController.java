package com.szymonosicinski.tripplanner.Controller;

import com.szymonosicinski.tripplanner.DTO.ControlPointDTO.CreateControlPointDTO;
import com.szymonosicinski.tripplanner.Service.ControlPointService;
import com.szymonosicinski.tripplanner.Util.CurrentUser;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/{tripId}/Route")
public class ControlPointController {
    @Autowired
    ControlPointService controlPointService;

    @PutMapping("/ControlPointAdd")
    public ResponseEntity addControlPoint(@RequestBody @Valid CreateControlPointDTO createControlPointDTO,
                                          @PathVariable("tripId") final UUID tripId,
                                          @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(controlPointService.addControlPoint(createControlPointDTO,tripId,currentUser), HttpStatus.OK);
    }
}
