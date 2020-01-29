package com.szymonosicinski.tripplanner.DTO.ControlPointDTO;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class CreateControlPointDTO {

    //@NotNull
    @Length(max=50)
    String name;

    //@Pattern(regexp = "[0-9]{1,2}.[0-9]+[NS]")
    @NotNull
    float latitude;

    //@Pattern(regexp = "[0-9]{1,3}.[0-9]+[EW]")
    @NotNull
    float longitude;
}
