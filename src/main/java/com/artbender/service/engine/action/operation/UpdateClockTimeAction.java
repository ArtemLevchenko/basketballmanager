package com.artbender.service.engine.action.operation;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.exceptions.NbaManagerException;
import com.artbender.model.tendency.GameFocusTendency;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
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
@Order(2)
public class UpdateClockTimeAction implements Action {

    private final static List<Integer> SLOW_TIME = List.of(24, 20, 22, 22, 23, 20, 23, 22, 20, 24);
    private final static List<Integer> BALANCE_TIME = List.of(17, 19, 18, 19, 15, 15, 19, 18, 19, 15);
    private final static List<Integer> FAST_TIME = List.of(11, 12, 9, 12, 10, 10, 12, 8, 10, 9);

    private RandomService randomService;


    @Autowired
    public UpdateClockTimeAction(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.UPDATE_TIME;
    }

    @Override
    public void execute(GameContext gameContext) {
        GameFocusTendency gameFocusTendency = gameContext.getActionParams().getOffensiveCoach().getCoachGamePlan().getOffenseDefense();
        int timeShift = getTimeShiftPerAttack(gameFocusTendency);
        log.debug("Time Shift = {}", timeShift);
        gameContext.getGameClock().setRangeStep(timeShift);

        gameContext.getGameClock().setSeconds(timeShift);
        if (gameContext.getGameClock().getMinutes() <= -1) {
            gameContext.getGameClock().setPeriod(gameContext.getGameClock().getPeriod() + 1);
            // check overtime
            if (gameContext.getGameClock().getPeriod() >= GameConstants.OVERTIME_PERIOD) {
                if (gameContext.getActionParams().getHomeStats().get_PTS()
                        == gameContext.getActionParams().getAwayStats().get_PTS()) {
                    gameContext.getGameClock().setOvertime();
                } else { // end game
                    gameContext.getGameClock().setFinished(Boolean.TRUE);
                }
            } else { // next quarter
                gameContext.getGameClock().setQuarter();
            }
        }
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        if (gameContext.getGameClock().isFinished()) {
            gameContext.setCurrentAction(GameAction.END_GAME);
        } else {
            gameContext.setCurrentAction(GameAction.UPDATE_STAMINA);
        }
    }

    private int getTimeShiftPerAttack(GameFocusTendency gameFocusTendency) {
        int timeIndex = randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND);
        if (gameFocusTendency == GameFocusTendency.DEFENSIVE) {
            return SLOW_TIME.get(timeIndex);
        } else if (gameFocusTendency == GameFocusTendency.BALANCE) {
            return BALANCE_TIME.get(timeIndex);
        } else if (gameFocusTendency == GameFocusTendency.OFFENSIVE) {
            return FAST_TIME.get(timeIndex);
        }
        throw new NbaManagerException("Time list NOT found. Please check coach settings.");
    }
}
