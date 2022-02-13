package com.artbender.service.transformation.converter;

import java.util.List;

public interface ConverterStrategy<T> {
    List<T> loadData();
}

