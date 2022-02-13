package com.artbender.service.engine.action.operation;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
import com.artbender.model.dto.logic.PlayerWeightDTO;
import com.artbender.model.exceptions.NbaManagerException;
import com.artbender.model.tendency.SupportFocusTendency;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.action.support.WeightComparator;
import com.artbender.service.engine.context.GameContext;
import com.artbender.service.engine.random.RandomService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Slf4j
@Service
@Order(12)
public class MissAndReboundAction implements Action {

    //# 1 - offense rebound
    private final static List<Integer> OFFENSE_REBOUND_LIST = List.of(1, 0, 1, 0, 0, 0, 1, 0, 0, 0);

    private final static List<Integer> REBOUND_REBOUND_DOMINATE = List.of(150, 150, 120, 120, 140, 150, 100, 120, 130, 140);
    private final static List<Integer> REBOUND_BALANCE_DOMINATE = List.of(100, 100, 100, 110, 120, 100, 100, 90, 100, 110);
    private final static List<Integer> REBOUND_ASSIST_DOMINATE = List.of(70, 80, 80, 90, 70, 50, 70, 60, 80, 80);

    private final static List<Integer> REBOUND_SIZE = List.of(0,0,1,0,0,1,0,1,2,3);


    private RandomService randomService;

    @Autowired
    public MissAndReboundAction(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.MISS;
    }

    @Override
    public void execute(GameContext gameContext) {
        gameContext.getActionGameEventListener().setScore(0);
        gameContext.getActionGameEventListener().putActions(gameContext.getActionGameEventListener().getOffensivePlayerWeightList().get(0).getPlayer(),
                gameContext.getCurrentAction());
        GameAction reboundAction = OFFENSE_REBOUND_LIST.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND)) == 1 ? GameAction.OFREBOUND
                : GameAction.DREBOUND;
        Player plRebound;
        if (reboundAction == GameAction.OFREBOUND) {
            plRebound = getPlayerWithRebound(reboundAction, gameContext.getActionParams().getOffensivePlayers());
        } else {
            plRebound = getPlayerWithRebound(reboundAction, gameContext.getActionParams().getDefensivePlayers());
        }
        gameContext.getActionGameEventListener().putActions(plRebound, reboundAction);
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.UPDATE_STATS);
    }

    private Player getPlayerWithRebound(GameAction reboundAction, List<Player> players) {
        List<PlayerWeightDTO> listPlayers = new ArrayList<>();
        for (Player pl : players) {
            List<Integer> reboundList = getReboundList(pl);
            int weightResult = reboundList.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
            int rebound;
            if (reboundAction == GameAction.OFREBOUND) {
                rebound = pl.getCharacteristics().getOfRebound();
            } else {
                rebound = pl.getCharacteristics().getDefRebound();
            }
            log.debug("weightResult= {} ", weightResult);
            log.debug("rebound= {} ", rebound);
            listPlayers.add(new PlayerWeightDTO(rebound + weightResult, pl));
        }
        listPlayers.sort(new WeightComparator());
        int indexWinReb = REBOUND_SIZE.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        return listPlayers.get(indexWinReb).getPlayer();
    }

    private List<Integer> getReboundList(Player player) {
        if (player.getPlayerGamePlan().getReboundAssist() == SupportFocusTendency.REBOUND) {
            return REBOUND_REBOUND_DOMINATE;
        } else if (player.getPlayerGamePlan().getReboundAssist() == SupportFocusTendency.BALANCE) {
            return REBOUND_BALANCE_DOMINATE;
        } else if (player.getPlayerGamePlan().getReboundAssist() == SupportFocusTendency.ASSIST) {
            return REBOUND_ASSIST_DOMINATE;
        }
        throw new NbaManagerException("Rebound list is not found. Please correct it.");
    }
}
