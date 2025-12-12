package com.yt.jpa.hospitalManagement.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MapperConfig {


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}