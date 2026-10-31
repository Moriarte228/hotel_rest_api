package com.vyshnikova.propertyview.controller;

import com.vyshnikova.propertyview.service.HistogramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Histogram")
@RestController
public class HistogramController {

    private final HistogramService service;

    public HistogramController(HistogramService service) {
        this.service = service;
    }

    @Operation(summary = "Count hotels grouped by brand, city, country or amenities")
    @GetMapping("/histogram/{param}")
    public Map<String, Long> histogram(@PathVariable String param) {
        return service.histogram(param);
    }
}
