package com.artbender.web.controllers;

import com.artbender.model.dto.rest.*;
import com.artbender.service.orchestrator.GameOrchestrator;
import com.artbender.web.constants.GameAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NbaSimulateGameController {

    private final GameOrchestrator gameOrchestrator;

    public NbaSimulateGameController(GameOrchestrator gameOrchestrator) {
        this.gameOrchestrator = gameOrchestrator;
    }

    @PostMapping(value = GameAPI.MAKE_TURN)
    public ResponseEntity<GlobalGameResponse> execute() {
        return ResponseEntity.accepted().body(gameOrchestrator.execute());
    }

    @PostMapping(value = GameAPI.INIT)
    public ResponseEntity<GlobalGameResponse> initGame(@RequestBody GlobalGameRequest teamGameRequest) {
        return ResponseEntity.accepted().body(gameOrchestrator.initGame(teamGameRequest));
    }

    @GetMapping(value = GameAPI.PLAYER_INFO)
    public ResponseEntity<IndividualPlayerCardResponse> playerInfo(@RequestParam Integer id) {
        return ResponseEntity.accepted().body(gameOrchestrator.getPlayerCardInfo(id));
    }

    @PutMapping(value = GameAPI.SAVE_PLAYER_SETTINGS)
    public ResponseEntity<GlobalGameResponse> savePlayerSettings(@RequestBody IndividualPlayerCardRequest individualPlayerCardRequest) {
        return ResponseEntity.ok().body(gameOrchestrator.savePlayerSettings(individualPlayerCardRequest));
    }

    @GetMapping(value = GameAPI.COACH_INFO)
    public ResponseEntity<CoachSettingDTO> savePlayerSettings(@RequestParam Integer id) {
        return ResponseEntity.accepted().body(gameOrchestrator.getCoachCardInfo(id));
    }

    @PutMapping(value = GameAPI.SAVE_COACH_SETTINGS)
    public ResponseEntity<Void> saveCoachSettings(@RequestBody CoachSettingDTO coachSettingDTO) {
        gameOrchestrator.saveCoachSettings(coachSettingDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = GameAPI.TIMEOUT)
    public ResponseEntity<GlobalGameResponse> timeout(@RequestBody CoachSettingDTO coachSettingDTO) {
        return ResponseEntity.accepted().body(gameOrchestrator.timeout(coachSettingDTO));
    }
}
