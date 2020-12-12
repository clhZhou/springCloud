package com.luis.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luis
 * @date 2020/10/27
 */
@RestController
public class SysUserController {

    @GetMapping("/login")
    public void login(@RequestParam String username,@RequestParam String password){
        System.out.println("come in ");
    }
}
