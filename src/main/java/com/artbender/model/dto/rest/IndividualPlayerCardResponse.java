package com.artbender.model.dto.rest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderClassName = "Builder")
public class IndividualPlayerCardResponse {
    private String insideOutside;
    private String offenseDefense;
    private String reboundAssist;
    private String screenOpening;
    private boolean inStart;
}
