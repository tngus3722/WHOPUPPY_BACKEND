package com.whopuppy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableScheduling
public class WhoPuppyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhoPuppyApplication.class, args);
    }

}
