package com.artbender.service.engine.action.operation;

import com.artbender.model.db.Player;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.calculator.substitution.SubstitutionService;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.context.GameContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@Slf4j
@Service
@Order(16)
public class AutoSubstitutionAction implements Action {
    private SubstitutionService substitutionService;

    @Autowired
    public AutoSubstitutionAction(SubstitutionService substitutionService) {
        this.substitutionService = substitutionService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.AUTO_SUBSTITUTION;
    }

    @Override
    public void execute(GameContext gameContext) {
        List<Player> playersAway = gameContext.getActionParams().getAwayPlayers();
        List<Player> playersHome = gameContext.getActionParams().getHomePlayers();

        substitutionService.execute(playersHome, gameContext.getActionParams().getHomeCoach().getCoachGamePlan().getSubstitutionRating(), gameContext.getGameClock());
        substitutionService.execute(playersAway, gameContext.getActionParams().getAwayCoach().getCoachGamePlan().getSubstitutionRating(), gameContext.getGameClock());
        log.debug("Executed AutoSubstitutionAction");
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.RESULT);
    }
}
