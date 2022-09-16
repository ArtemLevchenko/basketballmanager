package com.artbender.service.engine.action.operation;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.action.support.OnBall;
import com.artbender.service.engine.action.support.Schema;
import com.artbender.service.engine.context.GameContext;
import com.artbender.service.engine.event.ActionGameEventListener;
import com.artbender.service.engine.random.RandomService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


@NoArgsConstructor
@Slf4j
@Service
@Order(17)
public class NextTurnUpdateAction implements Action {

    private RandomService randomService;

    @Autowired
    public NextTurnUpdateAction(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.RESULT;
    }

    @Override
    public void execute(GameContext gameContext) {
        addPlayByPlayMessages(gameContext);
        OnBall currentOnBall = getNextOnCort(gameContext.getActionGameEventListener());
        gameContext.setActionGameEventListener(new ActionGameEventListener(currentOnBall));
    }

    private OnBall getNextOnCort(ActionGameEventListener actionGameEventListener) {
        for (Player player : actionGameEventListener.getPlayerActionsMap().keySet()) {
            if (actionGameEventListener.getPlayerActionsMap().get(player).contains(GameAction.OFREBOUND)) {
                return actionGameEventListener.getOnBall();
            }
        }
        return getNextOnCort(actionGameEventListener.getOnBall());
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.INIT);
    }

    private OnBall getNextOnCort(OnBall onBallNow) {
        return onBallNow == OnBall.HOME ? OnBall.AWAY : OnBall.HOME;
    }


    private void scoreResult(StringBuilder messageBuilder, Schema schema, Player player) {
        if (schema == Schema.INSIDE) {
            int result = randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND);
            if (result > 5) {
                messageBuilder.append("Slam Dunk ").append(player.getName())
                        .append(" (")
                        .append(player.getPlayerStats().get_PTS())
                        .append(" PTS)");
                messageBuilder.append("\n");
            } else {
                messageBuilder.append("Lay Up ").append(player.getName())
                        .append(" (")
                        .append(player.getPlayerStats().get_PTS())
                        .append(" PTS)");
                messageBuilder.append("\n");
            }
        } else if (schema == Schema.MID) {
            messageBuilder.append("Jump Shot ").append(player.getName())
                    .append(" (")
                    .append(player.getPlayerStats().get_PTS())
                    .append(" PTS)");
            messageBuilder.append("\n");
        } else if (schema == Schema.THREEPT) {
            messageBuilder.append("3PT ").append(player.getName())
                    .append(" (")
                    .append(player.getPlayerStats().get_PTS())
                    .append(" PTS)");
            messageBuilder.append("\n");
        }
    }

    // TODO: update new mechanism
    public void addPlayByPlayMessages(GameContext gameContext) {
        int period = gameContext.getGameClock().getPeriod();
        String currentTime = gameContext.getGameClock().getFormatMinutes() + ":" + gameContext.getGameClock().getFormatSeconds();
        Schema schema = gameContext.getActionGameEventListener().getSchema();
        StringBuilder messageBuilder = new StringBuilder();
        for (var entry : gameContext.getActionGameEventListener().getPlayerActionsMap().entrySet()) {
            for (GameAction gameAction : entry.getValue()) {
                messageBuilder.append("Q: ").append(period);
                messageBuilder.append("\n");
                messageBuilder.append("Time: ").append(currentTime);
                messageBuilder.append("\n");
                if (gameAction == GameAction.SCORE) {
                    scoreResult(messageBuilder, schema, entry.getKey());
                } else if (gameAction == GameAction.TURNOVER) {
                    messageBuilder.append(entry.getKey().getName()).append(" Lost Ball")
                            .append(" (")
                            .append(entry.getKey().getPlayerStats().get_TO())
                            .append(" TO)");
                    messageBuilder.append("\n");
                } else if (gameAction == GameAction.DREBOUND) {
                    messageBuilder.append(entry.getKey().getName()).append(" Def. Rebound")
                            .append(" (")
                            .append(entry.getKey().getPlayerStats().get_DEFR())
                            .append(" D.REB)");
                    messageBuilder.append("\n");
                } else if (gameAction == GameAction.OFREBOUND) {
                    messageBuilder.append(entry.getKey().getName()).append(" Off. Rebound")
                            .append(" (")
                            .append(entry.getKey().getPlayerStats().get_OFFR())
                            .append(" OF.REB)");
                    messageBuilder.append("\n");
                } else if (gameAction == GameAction.ASSIST) {
                    messageBuilder.append(entry.getKey().getName()).append(" ASSIST")
                            .append(" (")
                            .append(entry.getKey().getPlayerStats().get_AST())
                            .append(" AST)");
                    messageBuilder.append("\n");
                } else if (gameAction == GameAction.STEAL) {
                    messageBuilder.append(entry.getKey().getName()).append(" STEAL")
                            .append(" (")
                            .append(entry.getKey().getPlayerStats().get_ST())
                            .append(" STL)");
                    messageBuilder.append("\n");
                } else if (gameAction == GameAction.BLOCK) {
                    messageBuilder.append(entry.getKey().getName()).append(" BLOCK")
                            .append(" (")
                            .append(entry.getKey().getPlayerStats().get_BS())
                            .append(" BLK)");
                    messageBuilder.append("\n");
                } else if (gameAction == GameAction.FREETHROW) {
                    messageBuilder.append(entry.getKey().getName()).append(" FREE THROW")
                            .append(" (")
                            .append(entry.getKey().getPlayerStats().get_PTS())
                            .append(" PTS)");
                    messageBuilder.append("\n");
                } else if (gameAction == GameAction.MISS) {
                    messageBuilder.append(entry.getKey().getName()).append(" MISS SHOT");
                    messageBuilder.append("\n");
                } else if (gameAction == GameAction.FOUL) {
                    messageBuilder.append(entry.getKey().getName()).append(" P.FOUL")
                            .append(" (")
                            .append(entry.getKey().getPlayerStats().get_PF())
                            .append(" PF)");
                    messageBuilder.append("\n");
                }
            }
        }
        gameContext.getGameMessageTray().setActiveMessage(messageBuilder.toString());
        gameContext.getGameMessageTray().getPoolMessages().add(messageBuilder.toString());
    }
}
