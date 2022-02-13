package com.artbender.service.engine.action.operation;

import com.artbender.model.db.Coach;
import com.artbender.model.db.Player;
import com.artbender.model.dto.logic.PlayerWeightDTO;
import com.artbender.model.support.GamePosition;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.calculator.defensive.DefensiveCalculator;
import com.artbender.service.engine.action.calculator.defensive.other.DefensiveIndividualWeightCalculator;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.action.support.Schema;
import com.artbender.service.engine.context.GameContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.*;

@NoArgsConstructor
@Slf4j
@Service
@Order(6)
public class DefensiveAction implements Action {

    private DefensiveIndividualWeightCalculator defensiveIndividualWeightCalculator;
    private List<DefensiveCalculator> defensivePlayerWeightCalculators;

    @Autowired
    public DefensiveAction(DefensiveIndividualWeightCalculator defensiveIndividualWeightCalculator, List<DefensiveCalculator> defensivePlayerWeightCalculators) {
        this.defensiveIndividualWeightCalculator = defensiveIndividualWeightCalculator;
        this.defensivePlayerWeightCalculators = defensivePlayerWeightCalculators;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.DEFENSE;
    }

    @Override
    public void execute(GameContext gameContext) {
        // 1 - GET ATTACK positions
        Set<GamePosition> attackerGamePositionSet = new HashSet<>();
        for (PlayerWeightDTO playerWeightDTO : gameContext.getActionGameEventListener().getOffensivePlayerWeightList()) {
            attackerGamePositionSet.add(playerWeightDTO.getPlayer().getCurrentGamePosition());
        }
        // 2 - GET Defense Players by Attack positions + size
        List<Player> defensivePlayersByTurn = getDefensivePlayersByAttackGamePositions(gameContext.getActionParams().getDefensivePlayers(),
                attackerGamePositionSet, gameContext.getActionGameEventListener().getOffensivePlayerWeightList().size());
        List<PlayerWeightDTO> defensivePlayerWeightSet = new ArrayList<>();
        Schema schema = gameContext.getActionGameEventListener().getSchema();
        for (Player player : defensivePlayersByTurn) {
            int weight = calculateWeightForPlayer(player, schema, gameContext.getActionParams().getDefensiveCoach());
            log.debug("Defensive Weight = {} ", weight);
            defensivePlayerWeightSet.add(new PlayerWeightDTO(weight, player));
        }
        gameContext.getActionGameEventListener().setDefensivePlayerWeightList(defensivePlayerWeightSet);
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.TEAM_COMPARE);
    }

    private int calculateWeightForPlayer(Player player, Schema schema, Coach coach) {
        int weight = 0;
        for (DefensiveCalculator calculator : defensivePlayerWeightCalculators) {
            if (calculator.applicable(schema)) {
                weight += calculator.calculate(player);
            }
        }
        log.debug("weight= {} ", weight);
        int finalWeight = defensiveIndividualWeightCalculator.calculate(coach, player);
        log.debug("preFinal= {} ", finalWeight);
        return weight + finalWeight;
    }

    private List<Player> getDefensivePlayersByAttackGamePositions(List<Player> defensivePlayers, Set<GamePosition> attackerGamePositionList, int sizeAttack) {
        if (sizeAttack == 5) {
            return defensivePlayers;
        }
        List<Player> freePlayerByPosition = new ArrayList<>();
        List<Player> defPlayersByPositions = new ArrayList<>();
        for(Player player : defensivePlayers) {
            if(attackerGamePositionList.contains(player.getCurrentGamePosition())) {
                defPlayersByPositions.add(player);
                attackerGamePositionList.remove(player.getCurrentGamePosition());
            } else {
                freePlayerByPosition.add(player);
            }
        }
        if (defPlayersByPositions.size() != sizeAttack) {
            for (int i = 0; i < sizeAttack - defPlayersByPositions.size(); i++) {
                defPlayersByPositions.add(freePlayerByPosition.get(i));
            }
            return defPlayersByPositions;
        }
        return defPlayersByPositions;
    }
}
