package com.tjufe.graduate.lbsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class LbsserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbsserverApplication.class, args);
    }
}
