package com.qihang.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@ComponentScan(basePackages={"com.qihang"})
@SpringBootApplication
public class Api {
    public static void main( String[] args )
    {
        SpringApplication.run(Api.class, args);
    }
}
