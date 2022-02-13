package com.artbender.service.transformation.loader;

import com.artbender.model.db.Coach;
import com.artbender.service.transformation.converter.CoachConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Component
public class CoachLoader implements LoaderStrategy<Coach> {
    private final String LOAD_PATH_START = "/game/coach/";
    private final String LOAD_PATH_END = "_coach.xml";

    private CoachConverter coachConverter;

    @Override
    public List<Coach> loadData(String pathName) {
        coachConverter.setFileOut(CoachConverter.class.getResourceAsStream(LOAD_PATH_START + pathName + LOAD_PATH_END));
        return coachConverter.loadData();
    }
}
