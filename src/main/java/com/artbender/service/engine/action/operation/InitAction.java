package com.artbender.service.engine.action.operation;

import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.context.GameContext;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
@Order(0)
public class InitAction extends AbstractGameAction implements Action {

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.INIT;
    }

    @Override
    public void execute(GameContext gameContext) {
        super.initPlayers(gameContext);
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        if (gameContext.getGameClock().isStarted()) {
            gameContext.setCurrentAction(GameAction.UPDATE_TIME);
        } else {
            gameContext.getGameClock().setStarted(Boolean.TRUE);
            gameContext.setCurrentAction(GameAction.FACEOFF);
        }

    }

}
