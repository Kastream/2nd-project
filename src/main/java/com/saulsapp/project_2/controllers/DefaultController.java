package com.saulsapp.project_2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping()
public class DefaultController {
    @GetMapping()
    public String index() {
        return "index";
    }
    
}
