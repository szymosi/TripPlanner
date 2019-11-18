package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.DTO.TripDTO.TripCreateDTO;
import com.szymonosicinski.tripplanner.Entity.Trip;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Exception.UserException;
import com.szymonosicinski.tripplanner.Repository.TripRepository;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TripService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TripRepository tripRepository;

    public Trip create(TripCreateDTO tripCreateDTO, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        Trip trip =modelMapper.map(tripCreateDTO,Trip.class);
        trip.setOrganizer(currentUser.getId());
        tripRepository.save(trip);
        return trip;
    }

    public Page<Trip> getByOrganizer(UserPrincipal currentUser, Pageable pageable){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        return tripRepository.findAllByOrganizer(currentUser.getId(),pageable);
    }

    public List<Trip> getByParticipant(UserPrincipal currentUser, Pageable pageable){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        return tripRepository.findAllByParticipant(currentUser.getId());
    }

    public void addParticipant(UUID tripId, UUID participantId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()->new RuntimeException(ExceptionMessage.RESOURCE_NOT_FOUND.toString()));
        if(!trip.getOrganizer().equals(currentUser.getId()))
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        tripRepository.saveParticipant(tripId,participantId);
        return;
    }

    public boolean isParticipant(UUID tripId, UserPrincipal currentUser){
        if(currentUser==null)
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        if(tripRepository.isParticipant(tripId, currentUser.getId())==0)
            return false;
        else
            return true;
    }

}
