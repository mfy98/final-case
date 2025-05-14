package com.mfy98.bookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class BookServiceApplication {
    static {
        Locale.setDefault(Locale.US);
    }
    public static void main(String[] args) {
        SpringApplication.run(BookServiceApplication.class, args);
    }

}
