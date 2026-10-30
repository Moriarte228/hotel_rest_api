package com.vyshnikova.propertyview.service;

import com.vyshnikova.propertyview.dto.HistogramEntry;
import com.vyshnikova.propertyview.exception.UnsupportedHistogramParamException;
import com.vyshnikova.propertyview.histogram.HistogramStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistogramService {

    private final Map<String, HistogramStrategy> strategies = new HashMap<>();

    public HistogramService(List<HistogramStrategy> strategies) {
        for (HistogramStrategy strategy : strategies) {
            this.strategies.put(strategy.param(), strategy);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Long> histogram(String param) {
        HistogramStrategy strategy = strategies.get(param.toLowerCase());
        if (strategy == null) {
            throw new UnsupportedHistogramParamException(param);
        }

        Map<String, Long> result = new LinkedHashMap<>();
        for (HistogramEntry entry : strategy.count()) {
            result.put(entry.value(), entry.count());
        }
        return result;
    }
}