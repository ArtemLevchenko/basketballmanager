package com.artbender.service.engine.action.operation;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
import com.artbender.model.dto.logic.PlayerWeightDTO;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.action.support.Schema;
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
@Order(10)
public class FreeThrowAction implements Action {

    private final static List<Integer> FOUL_SHORT_LIST = List.of(4, 3, 3, 4, 2, 1, 2, 0, 4, 4);
    private final Map<Integer, List<Integer>> FREE_THROW_SKILL_TO_RESULT_MAP = Map.of(
            5, List.of(0, 1, 0, 1, 1, 0, 1, 0, 1, 0),
            6, List.of(1, 1, 0, 1, 1, 0, 1, 0, 1, 0),
            7, List.of(1, 1, 0, 1, 1, 0, 1, 1, 1, 0),
            8, List.of(1, 1, 0, 1, 1, 1, 1, 1, 1, 0),
            9, List.of(1, 1, 0, 1, 1, 1, 1, 1, 1, 1),
            10, List.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));

    private RandomService randomService;

    @Autowired
    public FreeThrowAction(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.FREETHROW;
    }

    @Override
    public void execute(GameContext gameContext) {
        Player defensiveFoulPlayer = getFoulForPlayer(gameContext.getActionParams().getDefensivePlayers());
        log.debug("defensiveFoulPlayer = {}", defensiveFoulPlayer.getName());
        gameContext.getActionGameEventListener().putActions(defensiveFoulPlayer, GameAction.FOUL);
        Player offensivePlayer = gameContext.getActionGameEventListener().getOffensivePlayerWeightList().get(0).getPlayer();
        int score = getScoresByFreeThrow(offensivePlayer, gameContext.getActionGameEventListener().getSchema());
        gameContext.getActionGameEventListener().setScore(score);
        gameContext.getActionGameEventListener().putActions(offensivePlayer, gameContext.getCurrentAction());
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.UPDATE_STATS);
    }

    private Player getFoulForPlayer(List<Player> defensivePlayers) {
        List<PlayerWeightDTO> listPlayers = new ArrayList<>();
        defensivePlayers
                .forEach(pl -> listPlayers
                        .add(new PlayerWeightDTO(pl.getCharacteristics().getDefenseOnBall() + pl.getCharacteristics().getTotalRank(), pl)));
        listPlayers.sort(new WeightComparator());
        int indexWin = FOUL_SHORT_LIST.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        return listPlayers.get(indexWin).getPlayer();
    }


    public int getScoresByFreeThrow(Player attack, Schema schema) {
        int result = 0;
        int freeThrow = attack.getCharacteristics().getFreeThrow() / 10;
        int mass[] = null;
        for (int i = 0; i < getScoresBySchema(schema); i++) {
            if (freeThrow <= 4) {
                result += FREE_THROW_SKILL_TO_RESULT_MAP.get(5).get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
            } else {
                result += FREE_THROW_SKILL_TO_RESULT_MAP.get(freeThrow).get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
            }
        }
        return result;
    }

    private int getScoresBySchema(Schema schema) {
        int result = 0;
        if (schema == Schema.INSIDE || schema == Schema.MID) {
            result = 2;
        } else if (schema == Schema.THREEPT) {
            result = 3;
        } else {
            result = 2;
        }
        return result;
    }
}
