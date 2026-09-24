package com.example.customer;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CustomerApplication {

    public static void main(String[] args) {
        //SpringApplication.run(CustomerApplication.class, args);

        new SpringApplicationBuilder(CustomerApplication.class)
                //.bannerMode(Banner.Mode.OFF)
                .logStartupInfo(true)
                .run(args);
    }

}
