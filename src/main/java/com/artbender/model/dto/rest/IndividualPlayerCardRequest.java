package com.artbender.model.dto.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class IndividualPlayerCardRequest {

    private String id;
    private String insideOutside;
    private String offenseDefense;
    private String reboundAssist;
    private String screenOpening;
    private boolean inStart;
}
