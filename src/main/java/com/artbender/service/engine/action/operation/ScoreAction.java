package com.artbender.service.engine.action.operation;

import com.artbender.model.constants.GameConstants;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.action.support.Schema;
import com.artbender.service.engine.context.GameContext;
import com.artbender.service.engine.random.RandomService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@Slf4j
@Service
@Order(9)
public class ScoreAction implements Action {

    private final static List<Integer> ASSIST_NEEDED = List.of(1, 1, 1, 0, 1, 0, 1, 1, 0, 1);

    private RandomService randomService;

    @Autowired
    public ScoreAction(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.SCORE;
    }

    @Override
    public void execute(GameContext gameContext) {
        int score = getScoresBySchema(gameContext.getActionGameEventListener().getSchema());
        log.debug("score = {}", score);
        gameContext.getActionGameEventListener().setScore(score);
        gameContext.getActionGameEventListener()
                .putActions(gameContext.getActionGameEventListener().getOffensivePlayerWeightList().get(0).getPlayer(), gameContext.getCurrentAction());
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        if (ASSIST_NEEDED.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND)) == 1) {
            gameContext.setCurrentAction(GameAction.ASSIST);
        } else {
            gameContext.setCurrentAction(GameAction.UPDATE_STATS);
        }
    }

    public int getScoresBySchema(Schema schema) {
        return schema == Schema.THREEPT ? 3 : 2;
    }
}
