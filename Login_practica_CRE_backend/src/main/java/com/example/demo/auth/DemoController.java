package com.example.demo.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DemoController {
    @PostMapping(value = "demo")
    public String welcome() {return "Bienvenido desde un endpoint seguro";}
}