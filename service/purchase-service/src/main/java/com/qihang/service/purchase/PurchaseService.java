package com.qihang.service.purchase;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@EnableDubbo
//@ComponentScan(basePackages={"com.qihang"})
@SpringBootApplication
public class PurchaseService
{
    public static void main( String[] args )
    {
        System.out.println( "Hello Purchase-Service!" );
        SpringApplication.run(PurchaseService.class, args);
    }
}
