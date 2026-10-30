package com.vyshnikova.propertyview.controller;

import com.vyshnikova.propertyview.service.HistogramService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HistogramController {

    private final HistogramService service;

    public HistogramController(HistogramService service) {
        this.service = service;
    }

    @GetMapping("/histogram/{param}")
    public Map<String, Long> histogram(@PathVariable String param) {
        return service.histogram(param);
    }
}
