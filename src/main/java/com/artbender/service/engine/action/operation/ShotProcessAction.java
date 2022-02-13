package com.artbender.service.engine.action.operation;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
import com.artbender.model.dto.logic.PlayerWeightDTO;
import com.artbender.model.exceptions.NbaManagerException;
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
@Order(8)
public class ShotProcessAction implements Action {

    private final static List<Integer> _60_PERCENTAGE = List.of(1, 1, 1, 0, 1, 0, 1, 0, 1, 0);
    private final static List<Integer> _50_PERCENTAGE = List.of(1, 0, 1, 0, 1, 0, 1, 0, 1, 0);
    private final static List<Integer> _30_PERCENTAGE = List.of(1, 0, 1, 0, 0, 0, 1, 0, 0, 0);
    private final static List<Integer> _20_PERCENTAGE = List.of(1, 0, 1, 0, 0, 0, 0, 0, 0, 0);

    //# 1 - miss, 0 - other
    //# 0 - turnover, 1 - block, 2- steal

    private final static List<Integer> FREE_THROW_ATTEMPT = List.of(1, 0, 1, 0, 0, 0, 1, 0, 0, 0);
    private final static List<Integer> MISS_OR_OTHER = List.of(1, 0, 1, 0, 1, 0, 1, 0, 1, 1);
    private final static List<Integer> BLOCK_DOMINATION = List.of(0, 1, 0, 1, 2, 0, 1, 0, 1, 2);
    private final static List<Integer> STEAL_DOMINATION = List.of(0, 2, 0, 2, 1, 0, 2, 0, 2, 1);


    private RandomService randomService;

    @Autowired
    public ShotProcessAction(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.SHOOT_PROCESS;
    }

    @Override
    public void execute(GameContext gameContext) {
        PlayerWeightDTO offensivePlayerWeight = gameContext.getActionGameEventListener().getOffensivePlayerWeightList().get(0);
        log.debug("offensivePlayerWeight = {}", offensivePlayerWeight.getPlayer());
        PlayerWeightDTO defensivePlayerWeight = gameContext.getActionGameEventListener().getDefensivePlayerWeightList().get(0);
        log.debug("defensivePlayerWeight = {}", offensivePlayerWeight.getPlayer());
        List<Integer> shootProcessList = getShootProcessList(offensivePlayerWeight, defensivePlayerWeight, gameContext.getActionGameEventListener().getAttackDominate());
        GameAction result = shootProcessList.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND)) == 0
                ? GameAction.MISS : GameAction.SCORE;
        log.debug("Pre-finish Result = {}", result);
        gameContext.setCurrentAction(result);
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        PlayerWeightDTO defensivePlayerWeight = gameContext.getActionGameEventListener().getDefensivePlayerWeightList().get(0);
        GameAction nextGameAction = getNextGameActionByCurrentShootProcess(gameContext.getCurrentAction(), defensivePlayerWeight.getPlayer());
        log.debug("nextGameAction = {}", nextGameAction);
        gameContext.setCurrentAction(nextGameAction);
        // Result Possible values == GameAction.SCORE : GameAction.FREETHROW : GameAction.MISS : GameAction.TURNOVER : GameAction.BLOCK : GameAction.STEAL
    }

    private List<Integer> getShootProcessList(PlayerWeightDTO offensivePlayerWeight, PlayerWeightDTO defensivePlayerWeight, Boolean isAttackDominate) {
        if (isAttackDominate) {  // ATTACK DOMINATE -> 60%
            if (offensivePlayerWeight.getWeightPerAction() > defensivePlayerWeight.getWeightPerAction()) {  // 60% max
                return _60_PERCENTAGE;
            } else {  // 50 max
                return _50_PERCENTAGE;
            }
        } else { // Def dominate
            if (offensivePlayerWeight.getWeightPerAction() > defensivePlayerWeight.getWeightPerAction()) { // 30 max
                return _30_PERCENTAGE;
            } else { // 20 max
                return _20_PERCENTAGE;
            }
        }
    }

    private GameAction getNextGameActionByCurrentShootProcess(GameAction currentShootProcess, Player defensivePlayer) {
        if (currentShootProcess == GameAction.SCORE) {
            return FREE_THROW_ATTEMPT.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND)) == 0
                    ? GameAction.SCORE : GameAction.FREETHROW;
        }
        // miss
        GameAction result = MISS_OR_OTHER.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND)) == 0
                ? GameAction.OTHER : GameAction.MISS;
        // 6 % - BLOCK(1),  12 % - STEAL(2), 20%-TURNOVER(3)
        if (result == GameAction.OTHER) {
            int block = defensivePlayer.getCharacteristics().getBlock();
            int steal = defensivePlayer.getCharacteristics().getSteal();
            List<Integer> otherProcessResultList = block > steal ? BLOCK_DOMINATION : STEAL_DOMINATION;
            int resultValue = otherProcessResultList.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
            if (resultValue == 0) {
                return GameAction.TURNOVER;
            } else if (resultValue == 1) {
                return GameAction.BLOCK;
            } else if (resultValue == 2) {
                return GameAction.STEAL;
            }
            throw new NbaManagerException("Action: ShootProcess. Game Action is not found!");
        }
        return result;
    }
}
