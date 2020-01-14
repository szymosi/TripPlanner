package com.szymonosicinski.tripplanner.DTO.TripDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
}
