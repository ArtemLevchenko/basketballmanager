package com.artbender.service.engine.action.operation;

import com.artbender.model.db.Player;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.context.GameContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@Slf4j
@Service
@Order(18)
public class TimeoutAction implements Action {

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.HOME_TIMEOUT || gameContext.getCurrentAction() == GameAction.AWAY_TIMEOUT;
    }

    @Override
    public void execute(GameContext gameContext) {
        checkAndUpdateStaminaOnStartQuarter(gameContext.getActionParams().getHomePlayers());
        checkAndUpdateStaminaOnStartQuarter(gameContext.getActionParams().getAwayPlayers());
        if (gameContext.getCurrentAction() == GameAction.HOME_TIMEOUT) {
            gameContext.getActionParams().getHomeStats().setTimeouts(gameContext.getActionParams().getHomeStats().getTimeouts() - 1);
        } else if (gameContext.getCurrentAction() == GameAction.AWAY_TIMEOUT) {
            gameContext.getActionParams().getAwayStats().setTimeouts(gameContext.getActionParams().getAwayStats().getTimeouts() - 1);
        }
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.INIT);
    }

    private void checkAndUpdateStaminaOnStartQuarter(List<Player> playersToUpdateList) {
        for (Player pl : playersToUpdateList) {
            int fullStamina = pl.getCharacteristics().getStamina();
            int difference = pl.getCharacteristics().getCurrentStamina() + 9;
            pl.getCharacteristics().setCurrentStamina(Math.min(difference, fullStamina));
        }
    }

}
