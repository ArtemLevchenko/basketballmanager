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
import java.util.Map;

@NoArgsConstructor
@Slf4j
@Service
@Order(11)
public class AssistAction implements Action {

    private final static List<Integer> ASSIST_REBOUND_DOMINATE = List.of(70, 80, 80, 90, 70, 50, 70, 60, 80, 80);
    private final static List<Integer> ASSIST_BALANCE_DOMINATE = List.of(100, 100, 100, 110, 120, 100, 100, 90, 100, 110);
    private final static List<Integer> ASSIST_ASSIST_DOMINATE = List.of(150, 150, 120, 120, 140, 150, 100, 120, 130, 140);

    private final Map<Integer, List<Integer>> PLAYER_SIZE_MAP = Map.of(
            1, List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            2, List.of(1, 0, 1, 0, 1, 0, 1, 0, 1, 0),
            3, List.of(1, 0, 1, 0, 1, 0, 2, 0, 2, 0),
            4, List.of(0, 0, 1, 0, 0, 1, 0, 1, 2, 3),
            5, List.of(0, 1, 0, 2, 0, 3, 2, 1, 1, 4));

    private RandomService randomService;

    @Autowired
    public AssistAction(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.ASSIST;
    }

    @Override
    public void execute(GameContext gameContext) {
        Player assistPlayer = getAssistPlayer(gameContext.getActionParams().getOffensivePlayers(),
                gameContext.getActionGameEventListener().getOffensivePlayerWeightList().get(0).getPlayer());
        gameContext.getActionGameEventListener().putActions(assistPlayer, gameContext.getCurrentAction());
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.UPDATE_STATS);
    }

    public Player getAssistPlayer(List<Player> players, Player shootingPlayer) {
        List<PlayerWeightDTO> listPlayers = new ArrayList<>();
        for (Player pl : players) {
            if (shootingPlayer.getId() != pl.getId()) {
                List<Integer> resultWeightList = getAssistList(pl);
                int weightResult = resultWeightList.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
                listPlayers.add(new PlayerWeightDTO(pl.getCharacteristics().getPass() + weightResult, pl));
            }

        }
        listPlayers.sort(new WeightComparator());
        int index = PLAYER_SIZE_MAP.get(listPlayers.size()).get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        log.debug("index= {} ", index);
        return listPlayers.get(index).getPlayer();
    }


    private List<Integer> getAssistList(Player player) {
        if (player.getPlayerGamePlan().getReboundAssist() == SupportFocusTendency.REBOUND) {
            return ASSIST_REBOUND_DOMINATE;
        } else if (player.getPlayerGamePlan().getReboundAssist() == SupportFocusTendency.BALANCE) {
            return ASSIST_BALANCE_DOMINATE;
        } else if (player.getPlayerGamePlan().getReboundAssist() == SupportFocusTendency.ASSIST) {
            return ASSIST_ASSIST_DOMINATE;
        }
        throw new NbaManagerException("Assist list is not found. Please correct it.");
    }
}
