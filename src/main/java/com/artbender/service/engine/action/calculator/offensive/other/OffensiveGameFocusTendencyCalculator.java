package com.artbender.service.engine.action.calculator.offensive.other;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
import com.artbender.model.tendency.GameFocusTendency;
import com.artbender.service.engine.action.calculator.offensive.OffensiveCalculator;
import com.artbender.service.engine.action.support.Schema;
import com.artbender.service.engine.random.RandomService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@Service
@Order(9)
public class OffensiveGameFocusTendencyCalculator implements OffensiveCalculator {

    private final static List<Integer> OFFENSIVE_FOCUS = List.of(100, 100, 90, 80, 100, 90, 100, 70, 80, 90);
    private final static List<Integer> BALANCE_FOCUS = List.of(50, 50, 55, 45, 35, 30, 40, 50, 45, 35);
    private final static List<Integer> DEFENSIVE_FOCUS = List.of(5, 5, 5, 8, 5, 5, 5, 8, 10, 12);

    private RandomService randomService;

    @Autowired
    public OffensiveGameFocusTendencyCalculator(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean applicable(Schema schema) {
        return schema == Schema.THREEPT || schema == Schema.MID || schema == Schema.INSIDE;
    }

    @Override
    public int calculate(Player player) {
        int weight = 0;
        if (player.getPlayerGamePlan().getOffenseDefense() == GameFocusTendency.OFFENSIVE) {
            weight += OFFENSIVE_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getOffenseDefense() == GameFocusTendency.BALANCE) {
            weight += BALANCE_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getOffenseDefense() == GameFocusTendency.DEFENSIVE) {
            weight += DEFENSIVE_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        }
        return weight;
    }

}
