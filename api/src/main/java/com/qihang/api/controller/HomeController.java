package com.qihang.api.controller;

import com.qihang.interfaces.sys.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @DubboReference
    private UserService userService;
    @GetMapping("/home")
    public Object home(){
//        return "HOME";
        return userService.getUserById(1L);
    }
}
