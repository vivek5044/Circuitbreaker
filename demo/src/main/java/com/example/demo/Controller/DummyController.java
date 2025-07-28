package com.example.demo.Controller;

import com.example.demo.Service.DummyService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    @PostConstruct
    public void logProxyInfo(){
        System.out.println("Controller class: "+this.getClass());
        System.out.println("Is proxy: "+this.getClass().getName().contains("$$"));
    }

    @Autowired
    DummyService dummyService;

    @PostMapping("/trigger")
    public String triggerService(@RequestBody String input){
        return dummyService.doSomethingDangerous(input);
    }
}

