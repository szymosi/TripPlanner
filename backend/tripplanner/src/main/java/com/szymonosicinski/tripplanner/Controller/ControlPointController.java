package com.szymonosicinski.tripplanner.Controller;

import com.szymonosicinski.tripplanner.DTO.ControlPointDTO.CreateControlPointDTO;
import com.szymonosicinski.tripplanner.Entity.ControlPoint;
import com.szymonosicinski.tripplanner.Service.ControlPointService;
import com.szymonosicinski.tripplanner.Util.CurrentUser;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Page<ControlPoint> getControlPoints(@PathVariable("tripId") final UUID tripId,
                                               @CurrentUser final UserPrincipal currentUser,
                                               @RequestParam(value = "page", defaultValue = "0") final int page,
                                               @RequestParam(value = "size", defaultValue = "100") final int pageSize){
        return controlPointService.getControlPoints(tripId, currentUser ,
                PageRequest.of(page,pageSize, Sort.Direction.ASC,"order"));
    }

    @GetMapping("/GetAll")
    public List<ControlPoint> getAllControlPoints(@PathVariable("tripId") final UUID tripId,
                                                  @CurrentUser final UserPrincipal currentUser){
        return  controlPointService.getAllControlPoints(tripId, currentUser);
    }

    @DeleteMapping("/ControlPointDelete")
    public ResponseEntity removeControlPoint(@RequestParam(value = "controlPointId", defaultValue = "0") final UUID controlPointId,
                                             @PathVariable("tripId") final UUID tripId,
                                             @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(controlPointService.removeControlPoint(tripId,controlPointId,currentUser),HttpStatus.OK);
    }

    @PutMapping("/ChangeOrder")
    public Page<ControlPoint> changeOrder(@RequestParam(value = "controlPointId") final UUID controlPointId,
                                          @RequestParam(value = "newPosition", defaultValue = "0") final int newPosition ,
                                          @PathVariable("tripId") final UUID tripId,
                                          @CurrentUser final UserPrincipal currentUser,
                                          @RequestParam(value = "page", defaultValue = "0") final int page,
                                          @RequestParam(value = "size", defaultValue = "100") final int pageSize){
        return controlPointService.changeOrder(tripId,controlPointId, newPosition,currentUser,
                PageRequest.of(page,pageSize, Sort.Direction.ASC,"id"));
    }
}
