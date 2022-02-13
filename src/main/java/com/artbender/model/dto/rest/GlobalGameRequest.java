package com.artbender.model.dto.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GlobalGameRequest {
    private String homeTeam;
    private String awayTeam;
}
