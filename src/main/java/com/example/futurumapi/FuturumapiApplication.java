package com.example.futurumapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "com.example.futurumapi")
@EnableAspectJAutoProxy
public class FuturumapiApplication {

    public static void main(String[] args) {
        System.setProperty("debug", "true"); // Enables security debugging
        SpringApplication.run(FuturumapiApplication.class, args);
    }

}
