package com.guitarshop.ui_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Value("${product.service.url}")
    private String productServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String home(Model model) {
        List<Map> guitars = Arrays.asList(
                restTemplate.getForObject(productServiceUrl, Map[].class)
        );
        model.addAttribute("guitars", guitars);
        return "home";
    }
}
