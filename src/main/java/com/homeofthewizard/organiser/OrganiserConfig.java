package com.homeofthewizard.organiser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrganiserConfig {

    @Value("${testProp}") private String prop;

    @Bean
    public Friend getFriend(){
        System.out.println("hello I am " + prop);
        return new Friend(prop);
    }
}
