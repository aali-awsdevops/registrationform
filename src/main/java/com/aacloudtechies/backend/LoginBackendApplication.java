package com.aacloudtechies.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoginBackendApplication {

    private static final Logger logger = LoggerFactory.getLogger(LoginBackendApplication.class);

    public static void main(String[] args) {
        logger.info("Starting LoginBackendApplication");
        SpringApplication.run(LoginBackendApplication.class, args);
    }
}
