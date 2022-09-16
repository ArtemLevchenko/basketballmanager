package com.artbender.service.orchestrator;

import com.artbender.model.db.Coach;
import com.artbender.model.db.Player;
import com.artbender.model.dto.rest.*;
import com.artbender.model.exceptions.NbaManagerException;
import com.artbender.model.plan.PlayerGamePlan;
import com.artbender.model.tendency.*;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.context.GameContext;
import com.artbender.service.transformation.rest.GameTransformDataService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor
@Setter
@Getter
@Service
public class GameOrchestratorImpl implements GameOrchestrator {

    private GameContext gameContext;
    private GameInitService gameInitService;
    private GameTransformDataService gameTransformDataService;
    private List<Action> actions;

    @Autowired
    public GameOrchestratorImpl(List<Action> actions, GameInitService gameInitService, GameTransformDataService gameTransformDataService) {
        this.actions = actions;
        this.gameInitService = gameInitService;
        this.gameTransformDataService = gameTransformDataService;
    }

    @Override
    public GlobalGameResponse execute() {
        if (isEndGame()) {
            return null;
        }
        for (Action action : actions) {
            if (action.isValidPreExecuteAction(gameContext)) {
                action.execute(gameContext);
                action.postExecuteAction(gameContext);
            }
        }
        return GlobalGameResponse.builder()
                .homePlayers(gameTransformDataService.buildPlayerResponseDto(gameContext.getActionParams().getHomePlayers()))
                .awayPlayers(gameTransformDataService.buildPlayerResponseDto(gameContext.getActionParams().getAwayPlayers()))
                .homeTeam(gameTransformDataService.buildTeamResponseDto(gameContext.getActionParams().getHomePlayers().get(0).getTeam().getShortName(),
                        gameContext.getActionParams().getHomeStats()))
                .awayTeam(gameTransformDataService.buildTeamResponseDto(gameContext.getActionParams().getAwayPlayers().get(0).getTeam().getShortName(),
                        gameContext.getActionParams().getAwayStats()))
                .gameEvent(gameTransformDataService.buildGameEvent(gameContext.getGameClock(), gameContext.getActionGameEventListener().getOnBall(), gameContext.getGameMessageTray().getActiveMessage()))
                .build();
    }

    @Override
    public GlobalGameResponse initGame(GlobalGameRequest teamGameRequest) {
        this.gameContext = gameInitService.initGameContext(teamGameRequest.getHomeTeam(), teamGameRequest.getAwayTeam());

        return GlobalGameResponse.builder()
                .homePlayers(gameTransformDataService.buildPlayerResponseDto(gameContext.getActionParams().getHomePlayers()))
                .awayPlayers(gameTransformDataService.buildPlayerResponseDto(gameContext.getActionParams().getAwayPlayers()))
                .homeTeam(gameTransformDataService.buildTeamResponseDto(gameContext.getActionParams().getHomePlayers().get(0).getTeam().getShortName(),
                        gameContext.getActionParams().getHomeStats()))
                .awayTeam(gameTransformDataService.buildTeamResponseDto(gameContext.getActionParams().getAwayPlayers().get(0).getTeam().getShortName(),
                        gameContext.getActionParams().getAwayStats()))
                .gameEvent(gameTransformDataService.buildGameEvent(gameContext.getGameClock(), gameContext.getActionGameEventListener().getOnBall(), gameContext.getGameMessageTray().getActiveMessage()))
                .homeCoach(gameTransformDataService.buildCoachInfo(gameContext.getActionParams().getHomeCoach()))
                .awayCoach(gameTransformDataService.buildCoachInfo(gameContext.getActionParams().getAwayCoach()))
                .build();
    }

    @Override
    public IndividualPlayerCardResponse getPlayerCardInfo(Integer id) {

        List<Player> aimList = Stream.concat(gameContext.getActionParams().getHomePlayers().stream(), gameContext.getActionParams().getAwayPlayers().stream()).toList();
        Player currentPlayer = aimList.stream().filter(player -> player.getId() == id)
                .findFirst()
                .get();

        return IndividualPlayerCardResponse.builder()
                .inStart(currentPlayer.isInStart())
                .insideOutside(currentPlayer.getPlayerGamePlan().getInsideOutside().getLabel())
                .offenseDefense(currentPlayer.getPlayerGamePlan().getOffenseDefense().getLabel())
                .screenOpening(currentPlayer.getPlayerGamePlan().getScreenOpening().getLabel())
                .reboundAssist(currentPlayer.getPlayerGamePlan().getReboundAssist().getLabel())
                .build();
    }

    @Override
    public GlobalGameResponse savePlayerSettings(IndividualPlayerCardRequest individualPlayerCardRequest) {
        List<Player> aimList = Stream.concat(gameContext.getActionParams().getHomePlayers().stream(), gameContext.getActionParams().getAwayPlayers().stream()).toList();
        Player currentPlayer = aimList.stream().filter(player -> player.getId() == Integer.parseInt(individualPlayerCardRequest.getId()))
                .findFirst()
                .get();
        currentPlayer.setInStart(individualPlayerCardRequest.isInStart());
        PlayerGamePlan playerGamePlan = currentPlayer.getPlayerGamePlan();
        playerGamePlan.setInsideOutside(ShootingFocusTendency.valueOf(individualPlayerCardRequest.getInsideOutside()));
        playerGamePlan.setOffenseDefense(GameFocusTendency.valueOf(individualPlayerCardRequest.getOffenseDefense()));
        playerGamePlan.setScreenOpening(OffensiveFocusTendency.valueOf(individualPlayerCardRequest.getScreenOpening()));
        playerGamePlan.setReboundAssist(SupportFocusTendency.valueOf(individualPlayerCardRequest.getReboundAssist()));

        return GlobalGameResponse.builder()
                .homePlayers(gameTransformDataService.buildPlayerResponseDto(gameContext.getActionParams().getHomePlayers()))
                .awayPlayers(gameTransformDataService.buildPlayerResponseDto(gameContext.getActionParams().getAwayPlayers()))
                .build();
    }

    @Override
    public CoachSettingDTO getCoachCardInfo(Integer id) {
        if (gameContext.getActionParams().getHomeCoach().getId() == id) {
            return gameTransformDataService.buildCoachInfo(gameContext.getActionParams().getHomeCoach());
        } else if (gameContext.getActionParams().getAwayCoach().getId() == id) {
            return gameTransformDataService.buildCoachInfo(gameContext.getActionParams().getAwayCoach());
        }
        throw new NbaManagerException("Coach not found!");
    }

    @Override
    public void saveCoachSettings(CoachSettingDTO coachSettingDTO) {
        Coach coach = null;
        if (gameContext.getActionParams().getHomeCoach().getId() == coachSettingDTO.getId()) {
            coach = gameContext.getActionParams().getHomeCoach();
        } else if (gameContext.getActionParams().getAwayCoach().getId() == coachSettingDTO.getId()) {
            coach = gameContext.getActionParams().getAwayCoach();
        }
        if (coach == null) {
            throw new NbaManagerException("Coach not found! Save was failed");
        }
        coach.getCoachGamePlan().setOffenseDefense(GameFocusTendency.valueOf(coachSettingDTO.getOffenseDefense()));
        coach.getCoachGamePlan().setInsideOutside(ShootingFocusTendency.valueOf(coachSettingDTO.getInsideOutside()));
        coach.getCoachGamePlan().setSubstitutionRating(SubstitutionRating.convertValueToName(coachSettingDTO.getSubstitutionRating()));
    }

    @Override
    public GlobalGameResponse timeout(CoachSettingDTO coachSettingDTO) {
        if (gameContext.getActionParams().getHomeCoach().getId() == coachSettingDTO.getId()) {
            gameContext.setCurrentAction(GameAction.HOME_TIMEOUT);
        } else if (gameContext.getActionParams().getAwayCoach().getId() == coachSettingDTO.getId()) {
            gameContext.setCurrentAction(GameAction.AWAY_TIMEOUT);
        }
        return execute();
    }

    @Override
    public boolean isEndGame() {
        return gameContext.getCurrentAction() == GameAction.END_GAME;
    }

}
