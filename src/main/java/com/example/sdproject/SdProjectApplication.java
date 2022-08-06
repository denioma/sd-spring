package com.example.sdproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.example.sdproject.data.entity")
@EnableJpaRepositories(basePackages = "com.example.sdproject.data.repository")
@SpringBootApplication
public class SdProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdProjectApplication.class, args);
    }

}
