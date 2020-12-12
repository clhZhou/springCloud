package com.luis.system.util;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author luis
 * @date 2020/12/12
 */
@Component
@Aspect
public class TestAop {

    @Pointcut("execution(public * com.luis.system.controller.SysUserController.login(..))")
    public void login(){
        System.out.println("切面");
    }

    @Before("login()")
    public void beforeLogin(){
        System.out.println("before");
    }

    @After("login()")
    public void AfterLogin(){
        System.out.println("after");
    }

//    @Around("login()")
//    public void aRoundLogin(){
//        System.out.println("around");
//    }
}
