package edu.vestrin.wigelltravels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"edu.vestrin.wigelltravels", "com.groupc.shared"})
public class WigellTravelsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WigellTravelsApplication.class, args);
    }

}
