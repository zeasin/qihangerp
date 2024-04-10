package com.qihang.service.goods;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@EnableDubbo
//@ComponentScan(basePackages={"com.qihang"})
@SpringBootApplication
public class GoodsService
{
    public static void main( String[] args )
    {
        System.out.println( "Hello Goods-Service!" );
        SpringApplication.run(GoodsService.class, args);
    }
}
