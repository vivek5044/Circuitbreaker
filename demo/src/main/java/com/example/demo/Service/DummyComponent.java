package com.example.demo.Service;

import com.example.demo.exception.WrapperException;
import org.springframework.stereotype.Service;

@Service
public class DummyComponent {
    public String perform(String input){
        throw new WrapperException("Simulated Runtime exception");
    }
}
