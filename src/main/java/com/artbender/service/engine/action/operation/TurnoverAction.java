package com.artbender.service.engine.action.operation;

import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.context.GameContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Slf4j
@Service
@Order(13)
public class TurnoverAction implements Action {
    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.TURNOVER;
    }

    @Override
    public void execute(GameContext gameContext) {
        gameContext.getActionGameEventListener().setScore(0);
        gameContext.getActionGameEventListener().putActions(gameContext.getActionGameEventListener().getOffensivePlayerWeightList().get(0).getPlayer(),
                gameContext.getCurrentAction());
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.UPDATE_STATS);
    }
}
