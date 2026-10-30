package com.vyshnikova.propertyview.histogram;

import com.vyshnikova.propertyview.dto.HistogramEntry;

import java.util.List;

public interface HistogramStrategy {
    String param();
    List<HistogramEntry> count();
}