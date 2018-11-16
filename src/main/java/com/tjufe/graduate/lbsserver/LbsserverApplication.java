package com.tjufe.graduate.lbsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class LbsserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbsserverApplication.class, args);
    }
}
