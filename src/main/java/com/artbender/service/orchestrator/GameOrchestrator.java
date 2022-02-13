package com.artbender.service.orchestrator;

import com.artbender.model.dto.rest.*;

public interface GameOrchestrator {
    GlobalGameResponse execute();
    GlobalGameResponse initGame(GlobalGameRequest teamGameRequest);
    IndividualPlayerCardResponse getPlayerCardInfo(Integer id);
    GlobalGameResponse savePlayerSettings(IndividualPlayerCardRequest individualPlayerCardRequest);
    CoachSettingDTO getCoachCardInfo(Integer id);
    void saveCoachSettings(CoachSettingDTO coachSettingDTO);
    GlobalGameResponse timeout(CoachSettingDTO coachSettingDTO);
    boolean isEndGame();
}
