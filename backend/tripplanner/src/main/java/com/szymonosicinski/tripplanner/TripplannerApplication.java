package com.szymonosicinski.tripplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EnableSwagger2
@EnableWebSecurity
public class TripplannerApplication {


    public static void main(String[] args) {
        SpringApplication.run(TripplannerApplication.class, args);
    }


}
