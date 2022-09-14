package com.artbender.model.dto.rest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderClassName = "Builder")
public class CoachSettingDTO {
    private int id;
    private String name;
    private String insideOutside;
    private String offenseDefense;
    private int substitutionRating;
}
