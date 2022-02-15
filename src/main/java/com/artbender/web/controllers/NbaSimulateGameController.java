package com.artbender.web.controllers;

import com.artbender.model.dto.rest.*;
import com.artbender.model.exceptions.NbaManagerException;
import com.artbender.service.orchestrator.GameOrchestrator;
import com.artbender.web.constants.GameAPI;
import org.springframework.http.HttpStatus;
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
        try {
            return ResponseEntity.accepted().body(gameOrchestrator.execute());
        } catch (NbaManagerException nbaManagerException) {
            return new ResponseEntity(nbaManagerException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = GameAPI.INIT)
    public ResponseEntity<GlobalGameResponse> initGame(@RequestBody GlobalGameRequest teamGameRequest) {
        try {
            return ResponseEntity.accepted().body(gameOrchestrator.initGame(teamGameRequest));
        } catch (NbaManagerException nbaManagerException) {
            return new ResponseEntity(nbaManagerException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = GameAPI.PLAYER_INFO)
    public ResponseEntity<IndividualPlayerCardResponse> playerInfo(@RequestParam Integer id) {
        try {
            return ResponseEntity.accepted().body(gameOrchestrator.getPlayerCardInfo(id));
        } catch (NbaManagerException nbaManagerException) {
            return new ResponseEntity(nbaManagerException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = GameAPI.SAVE_PLAYER_SETTINGS)
    public ResponseEntity<GlobalGameResponse> savePlayerSettings(@RequestBody IndividualPlayerCardRequest individualPlayerCardRequest) {
        try {
            return ResponseEntity.ok().body(gameOrchestrator.savePlayerSettings(individualPlayerCardRequest));
        } catch (NbaManagerException nbaManagerException) {
            return new ResponseEntity(nbaManagerException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = GameAPI.COACH_INFO)
    public ResponseEntity<CoachSettingDTO> savePlayerSettings(@RequestParam Integer id) {
        try {
            return ResponseEntity.accepted().body(gameOrchestrator.getCoachCardInfo(id));
        } catch (NbaManagerException nbaManagerException) {
            return new ResponseEntity(nbaManagerException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = GameAPI.SAVE_COACH_SETTINGS)
    public ResponseEntity<Void> saveCoachSettings(@RequestBody CoachSettingDTO coachSettingDTO) {
        try {
            gameOrchestrator.saveCoachSettings(coachSettingDTO);
            return ResponseEntity.noContent().build();
        } catch (NbaManagerException nbaManagerException) {
            return new ResponseEntity(nbaManagerException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = GameAPI.TIMEOUT)
    public ResponseEntity<GlobalGameResponse> timeout(@RequestBody CoachSettingDTO coachSettingDTO) {
        try {
            return ResponseEntity.accepted().body(gameOrchestrator.timeout(coachSettingDTO));
        } catch (NbaManagerException nbaManagerException) {
            return new ResponseEntity(nbaManagerException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
