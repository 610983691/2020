package com.coulee.boot.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/example")
    public String example() {
        return "example:" + System.currentTimeMillis();
    }
}
