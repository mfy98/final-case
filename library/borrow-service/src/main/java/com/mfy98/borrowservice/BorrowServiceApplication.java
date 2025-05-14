package com.mfy98.borrowservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication()
@EnableJpaRepositories("com.mfy98.borrowservice.repository")
@EntityScan("com.mfy98.borrowservice.entity")
public class BorrowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BorrowServiceApplication.class, args);
    }

}
