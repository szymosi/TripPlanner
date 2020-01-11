package com.szymonosicinski.tripplanner.Controller;

import com.szymonosicinski.tripplanner.DTO.TripDTO.TripCreateDTO;
import com.szymonosicinski.tripplanner.Entity.Trip;
import com.szymonosicinski.tripplanner.Service.TripService;
import com.szymonosicinski.tripplanner.Util.CurrentUser;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/Trip")
public class TripController {

    @Autowired
    private TripService tripService;

    @PutMapping("/Create")
    public ResponseEntity createTrip(@RequestBody @Valid final TripCreateDTO tripCreateDTO,
                                     @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(tripService.create(tripCreateDTO,currentUser), HttpStatus.OK);
    }

    @PostMapping("/Update")
    @ResponseBody
    public ResponseEntity updateTrip(@RequestParam(value="tripId") final UUID tripId,
                                     @RequestBody @Valid final TripCreateDTO tripCreateDTO,
                                     @CurrentUser final UserPrincipal currentUser){
        return new ResponseEntity(tripService.update(tripId, tripCreateDTO, currentUser),HttpStatus.OK);
    }

    @GetMapping("/Organizer")
    public Page<Trip> getByOrganizer(@CurrentUser UserPrincipal currentUser,
                                     @RequestParam(value = "page", defaultValue = "0") final int page,
                                     @RequestParam(value = "size", defaultValue = "100") final int pageSize){
        return tripService.getByOrganizer(currentUser,
                PageRequest.of(page,pageSize, Sort.Direction.ASC,"id"));
    }

    @GetMapping("/Participant")
    @ResponseBody
    public Page<Trip> getByParticipant(@CurrentUser final UserPrincipal currentUser,
                                     @RequestParam(value = "page", defaultValue = "0") final int page,
                                     @RequestParam(value = "size", defaultValue = "100") final int pageSize){
        return tripService.getByParticipant(currentUser,
                PageRequest.of(page,pageSize, Sort.Direction.ASC,"id"));
    }

    @PutMapping("/{tripId}")
    public ResponseEntity addParticipant(@CurrentUser final UserPrincipal currentUser,
                                         @RequestParam(value = "participantId", defaultValue = "0") final UUID participantId,
                                         @PathVariable("tripId") UUID tripId){
        tripService.addParticipant(tripId,participantId,currentUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/isParticipant")
    public ResponseEntity isParticipant(@CurrentUser final UserPrincipal currentUser,
                                         @RequestParam(value = "tripId", defaultValue = "0") final UUID tripId){
        return new ResponseEntity(tripService.isParticipant(tripId,currentUser),HttpStatus.OK);
    }
}
