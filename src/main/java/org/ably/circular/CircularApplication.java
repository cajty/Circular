package org.ably.circular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CircularApplication {

    public static void main(String[] args) {
        SpringApplication.run(CircularApplication.class, args);
    }

}
