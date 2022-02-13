package com.artbender.model.dto.rest;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(builderClassName = "Builder")
public class GlobalGameResponse {
    private List<BoxScoresPlayerDTO> homePlayers;
    private List<BoxScoresPlayerDTO> awayPlayers;
    private BoxScoresTeamDTO homeTeam;
    private BoxScoresTeamDTO awayTeam;
    private GameEventDTO gameEvent;
    private CoachSettingDTO homeCoach;
    private CoachSettingDTO awayCoach;
}
