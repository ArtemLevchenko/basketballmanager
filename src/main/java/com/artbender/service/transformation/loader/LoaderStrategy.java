package com.artbender.service.transformation.loader;

import java.util.List;

public interface LoaderStrategy<T> {
    List<T> loadData(String pathName);
}
