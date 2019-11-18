package com.szymonosicinski.tripplanner.DTO.TripDTO;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
public class TripCreateDTO {

    @NotNull
    @Length(max=100,min=10)
    private String name;

    @Length(max=5000)
    private String descryption;

    private Date startDate;

    private Date endDate;
}
