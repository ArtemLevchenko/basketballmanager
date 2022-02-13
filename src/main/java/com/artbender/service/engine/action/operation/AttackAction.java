package com.artbender.service.engine.action.operation;

import com.artbender.model.db.Coach;
import com.artbender.model.db.Player;
import com.artbender.model.dto.logic.PlayerWeightDTO;
import com.artbender.model.exceptions.NbaManagerException;
import com.artbender.model.tendency.ShootingFocusTendency;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.calculator.offensive.OffensiveCalculator;
import com.artbender.service.engine.action.calculator.offensive.other.OffensiveIndividualWeightCalculator;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.action.support.Schema;
import com.artbender.service.engine.context.GameContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Slf4j
@Service
@Order(5)
public class AttackAction implements Action {

    private List<OffensiveCalculator> offensivePlayerWeightCalculators;
    private OffensiveIndividualWeightCalculator offensiveIndividualWeightCalculator;

    @Autowired
    public AttackAction(List<OffensiveCalculator> offensivePlayerWeightCalculators, OffensiveIndividualWeightCalculator offensiveIndividualWeightCalculator) {
        this.offensivePlayerWeightCalculators = offensivePlayerWeightCalculators;
        this.offensiveIndividualWeightCalculator = offensiveIndividualWeightCalculator;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.ATTACK;
    }

    @Override
    public void execute(GameContext gameContext) {
        Schema schema = gameContext.getActionGameEventListener().getSchema();
        // get applicable shootings by schema
        Set<ShootingFocusTendency> focusShootingList = getApplicableShootingsBySchema(schema);
        log.debug("focusShootingList= {} ", focusShootingList);
        List<Player> offensivePlayersPerTurnList = getOffensivePlayersPerTurnList(gameContext.getActionParams().getOffensivePlayers(), focusShootingList);
        List<PlayerWeightDTO> offensivePlayerWeightSet = new ArrayList<>();
        for (Player player : offensivePlayersPerTurnList) {
            int weight = calculateWeightForPlayer(player, schema, gameContext.getActionParams().getOffensiveCoach());
            log.debug("Offensive Weight = {} ", weight);
            offensivePlayerWeightSet.add(new PlayerWeightDTO(weight, player));
        }
        gameContext.getActionGameEventListener().setOffensivePlayerWeightList(offensivePlayerWeightSet);
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.DEFENSE);
    }

    private Set<ShootingFocusTendency> getApplicableShootingsBySchema(Schema schema) {
        if (schema == Schema.INSIDE) {
            return Set.of(ShootingFocusTendency.INSIDE, ShootingFocusTendency.BALANCE);
        } else if (schema == Schema.MID) {
            return Set.of(ShootingFocusTendency.BALANCE, ShootingFocusTendency.INSIDE);
        } else if (schema == Schema.THREEPT) {
            return Set.of(ShootingFocusTendency.THREEPT, ShootingFocusTendency.BALANCE);
        }
        throw new NbaManagerException("Action: AttackAction. Primary ShootingFocusTendency is not found!");
    }

    private List<Player> getOffensivePlayersPerTurnList(List<Player> offensivePlayers, Set<ShootingFocusTendency> focusShootingList) {
        List<Player> offensiveShortList = offensivePlayers.stream().filter(player -> focusShootingList.contains(player.getPlayerGamePlan().getInsideOutside())).collect(Collectors.toList());
        if (offensiveShortList.isEmpty()) { // if 0 player is available to shoot -> get random
            Collections.shuffle(offensivePlayers);
            offensiveShortList.add(offensivePlayers.get(0));
        }
        return offensiveShortList;
    }

    private int calculateWeightForPlayer(Player player, Schema schema, Coach coach) {
        int weight = 0;
        for (OffensiveCalculator calculator : offensivePlayerWeightCalculators) {
            if (calculator.applicable(schema)) {
                weight += calculator.calculate(player);
            }
        }
        log.debug("weight= {} ", weight);
        int finalWeight = offensiveIndividualWeightCalculator.calculate(coach, player);
        log.debug("coachWeight= {} ", finalWeight);
        return weight + finalWeight;
    }

}
