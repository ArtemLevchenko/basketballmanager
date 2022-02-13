package com.artbender.service.engine.action.operation;

import com.artbender.model.db.Player;
import com.artbender.service.engine.action.support.OnBall;
import com.artbender.service.engine.context.ActionParams;
import com.artbender.service.engine.context.GameContext;

import java.util.List;

public abstract class AbstractGameAction {

    protected void initPlayers(GameContext gameContext) {
        ActionParams actionParams = gameContext.getActionParams();
        if (gameContext.getActionGameEventListener().getOnBall() == OnBall.HOME) {
            actionParams.setOffensivePlayers(getInStartPlayers(actionParams.getHomePlayers()));
            actionParams.setDefensivePlayers(getInStartPlayers(actionParams.getAwayPlayers()));
            actionParams.setOffensiveCoach(actionParams.getHomeCoach());
            actionParams.setDefensiveCoach(actionParams.getAwayCoach());
        } else {
            actionParams.setOffensivePlayers(getInStartPlayers(actionParams.getAwayPlayers()));
            actionParams.setDefensivePlayers(getInStartPlayers(actionParams.getHomePlayers()));
            actionParams.setOffensiveCoach(actionParams.getAwayCoach());
            actionParams.setDefensiveCoach(actionParams.getHomeCoach());
        }
    }

    private List<Player> getInStartPlayers(List<Player> allPlayerList) {
        return allPlayerList.stream().filter(Player::isInStart).toList();
    }

    protected OnBall getNextOnBall(OnBall onBall) {
        return onBall == OnBall.HOME ? OnBall.AWAY : OnBall.HOME;
    }
}
