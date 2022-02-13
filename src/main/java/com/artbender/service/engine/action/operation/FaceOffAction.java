package com.artbender.service.engine.action.operation;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
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
@Service
@Order(1)
@Slf4j
public class FaceOffAction extends AbstractGameAction implements Action {

    private final static List<Integer> FACE_OFF_LIST = List.of(0, 1, 3, 2, 4, 7, 6, 4, 8, 10);
    private RandomService randomService;

    @Autowired
    public FaceOffAction(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.FACEOFF;
    }

    @Override
    public void execute(GameContext gameContext) {
        int currentIndexForAttack = randomService.randomIntegerIndex(GameConstants.PLAYER_INDEX_0, GameConstants.PLAYER_INDEX_4);
        int currentIndexForDefensive = randomService.randomIntegerIndex(GameConstants.PLAYER_INDEX_0, GameConstants.PLAYER_INDEX_4);

        Player faceOfAttackPlayer = gameContext.getActionParams().getOffensivePlayers().get(currentIndexForAttack);
        log.debug("faceOfAttackPlayer = {}", faceOfAttackPlayer.getName());

        Player faceOfDefensivePlayer = gameContext.getActionParams().getDefensivePlayers().get(currentIndexForDefensive);
        log.debug("faceOfDefensivePlayer = {}", faceOfDefensivePlayer.getName());

        int attackWeight = calculateWeight(faceOfAttackPlayer);
        int defensiveWeight = calculateWeight(faceOfDefensivePlayer);

        if (attackWeight < defensiveWeight) { // change onball
            log.debug("Change onBall attackWeight = {}, defensiveWeight = {}", attackWeight, defensiveWeight);
            // update ball
            gameContext.getActionGameEventListener().setOnBall(super.getNextOnBall(gameContext.getActionGameEventListener().getOnBall()));
            super.initPlayers(gameContext);
        }
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.UPDATE_TIME);
    }


    private int calculateWeight(Player player) {
        int randomWeight = FACE_OFF_LIST.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        return player.getCharacteristics().getOfRebound() + player.getCharacteristics().getDefRebound()
                + player.getCharacteristics().getTotalRank() + randomWeight;
    }
}
