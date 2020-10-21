package com.luis.system;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author luis
 * @date 2020/10/20
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/get")
    @ResponseBody
    public String get(){
        return "Hello_World";
    }
}
