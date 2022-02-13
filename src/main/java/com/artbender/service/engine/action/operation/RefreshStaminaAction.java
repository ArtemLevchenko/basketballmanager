package com.artbender.service.engine.action.operation;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.clock.GameClock;
import com.artbender.service.engine.context.ActionParams;
import com.artbender.service.engine.context.GameContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;


@NoArgsConstructor
@Slf4j
@Service
@Order(3)
public class RefreshStaminaAction extends AbstractGameAction implements Action {

    private final static int FULL_STAMINA = 200;
    private final static int QUARTER_STAMINA = 11;

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.UPDATE_STAMINA;
    }

    @Override
    public void execute(GameContext gameContext) {
        GameClock gameClock = gameContext.getGameClock();
        Integer currentTime = gameClock.getMinutes();
        if (currentTime == GameConstants.QUARTER_TIME || (currentTime == GameConstants.OVERTIME_TIME && gameContext.getGameClock().getPeriod() > 4)) {
            ActionParams actionParams = gameContext.getActionParams();
            checkAndUpdateStaminaOnStartQuarter(gameClock, actionParams.getHomePlayers());
            checkAndUpdateStaminaOnStartQuarter(gameClock, actionParams.getAwayPlayers());
            initPlayers(gameContext);
        }
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.SCHEMA);
    }

    private void checkAndUpdateStaminaOnStartQuarter(GameClock gameClock, List<Player> playersToUpdateList) {
        int refreshValue = gameClock.getPeriod() == GameConstants.HALF_TIME_PERIOD ? FULL_STAMINA : QUARTER_STAMINA;
        for (Player pl : playersToUpdateList) {
            int fullStamina = pl.getCharacteristics().getStamina();
            int difference = pl.getCharacteristics().getCurrentStamina() + refreshValue;
            pl.getCharacteristics().setCurrentStamina(Math.min(difference, fullStamina));
        }
    }

}
