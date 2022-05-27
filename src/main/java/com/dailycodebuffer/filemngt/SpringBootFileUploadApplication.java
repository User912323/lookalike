package com.dailycodebuffer.filemngt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.File;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SpringBootFileUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootFileUploadApplication.class, args);

    }

}
