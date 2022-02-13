package com.artbender.model.dto.rest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderClassName = "Builder")
public class GameEventDTO {
    private int quarter;
    private String time;
    private String onBall;
    private String lastEvent;
}
