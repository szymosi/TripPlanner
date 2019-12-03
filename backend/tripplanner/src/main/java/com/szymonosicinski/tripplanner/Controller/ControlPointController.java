package com.szymonosicinski.tripplanner.Controller;

import com.szymonosicinski.tripplanner.DTO.ControlPointDTO.CreateControlPointDTO;
import com.szymonosicinski.tripplanner.Entity.ControlPoint;
import com.szymonosicinski.tripplanner.Service.ControlPointService;
import com.szymonosicinski.tripplanner.Util.CurrentUser;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
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

    @GetMapping()
    public List<ControlPoint> getControlPoints(@PathVariable("tripId") final UUID tripId,
                                               @CurrentUser final UserPrincipal currentUser){
        return controlPointService.getControlPoints(tripId, currentUser);
    }

    @DeleteMapping("/ControlPointDelete")
    public ResponseEntity removeControlPoint(@RequestParam(value = "controlPointId", defaultValue = "0") final UUID controlPointId,
                                             @PathVariable("tripId") final UUID tripId,
                                             @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(controlPointService.removeControlPoint(tripId,controlPointId,currentUser),HttpStatus.OK);
    }

    @PostMapping("/ChangeOrder")
    public List<ControlPoint> changeOrder(@RequestParam(value = "controlPointId", defaultValue = "0") final UUID controlPointId,
                                          @RequestParam(value = "newPosition", defaultValue = "0") final int newPosition ,
                                          @PathVariable("tripId") final UUID tripId,
                                          @CurrentUser final UserPrincipal currentUser){
        return controlPointService.changeOrder(tripId,controlPointId, newPosition,currentUser);
    }
}
