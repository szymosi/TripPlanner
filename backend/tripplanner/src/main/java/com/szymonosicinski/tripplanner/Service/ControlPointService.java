package com.szymonosicinski.tripplanner.Service;

import com.szymonosicinski.tripplanner.DTO.ControlPointDTO.CreateControlPointDTO;
import com.szymonosicinski.tripplanner.Entity.ControlPoint;
import com.szymonosicinski.tripplanner.Entity.Trip;
import com.szymonosicinski.tripplanner.Exception.ExceptionMessage;
import com.szymonosicinski.tripplanner.Repository.ControlPointRepository;
import com.szymonosicinski.tripplanner.Util.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ControlPointService {

    @Autowired
    ControlPointRepository controlPointRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TripService tripService;

    public ControlPoint addControlPoint(CreateControlPointDTO createControlPointDTO, UUID tripId, UserPrincipal currentUser){
        if (currentUser == null)
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        Trip trip=tripService.getByIdAsOrganizer(tripId,currentUser);
        ControlPoint controlPoint=controlPointConverter(createControlPointDTO);
        controlPoint.setOrder(0/*trip.getControlPoints().size()*/);
        controlPoint.setTrip(trip);
        //trip.getControlPoints().add(controlPoint);
        controlPointRepository.save(controlPoint);
        return controlPoint;
    }

    public List<ControlPoint> getControlPoints(UUID tripId, UserPrincipal currentUser){
        if (currentUser == null)
            throw new RuntimeException(ExceptionMessage.ACCES_DENIED.toString());
        tripService.getById(tripId, currentUser);
        return controlPointRepository.findAllByTrip_Id(tripId);
    }

    private ControlPoint controlPointConverter(CreateControlPointDTO createControlPointDTO){
        ControlPoint controlPoint=new ControlPoint();

        controlPoint.setName(createControlPointDTO.getName());

        Pattern latitudePattern = Pattern.compile("([0-9]{1,2}.[0-9]+)([NS])");
        Pattern longitudePattern = Pattern.compile("([0-9]{1,3}.[0-9]+)([EW])");

        Matcher latitudeMatcher = latitudePattern.matcher(createControlPointDTO.getLatitude());
        Matcher longitudeMatcher = longitudePattern.matcher(createControlPointDTO.getLongitude());

        if(latitudeMatcher.matches() && longitudeMatcher.matches()){
            controlPoint.setLatitude(Double.valueOf(latitudeMatcher.group(1)));
            controlPoint.setNorthing(latitudeMatcher.group(2).equals("N"));

            controlPoint.setLongitude(Double.valueOf(longitudeMatcher.group(1)));
            controlPoint.setEasting(longitudeMatcher.group(2).equals("E"));

        }

        return controlPoint;
    }
}
