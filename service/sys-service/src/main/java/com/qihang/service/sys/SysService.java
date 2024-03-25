package com.qihang.service.sys;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@EnableDubbo
@SpringBootApplication
public class SysService
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(SysService.class, args);
    }
}
