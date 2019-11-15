package com.szymonosicinski.tripplanner.Configuration;

import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setAmbiguityIgnored(false)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}
