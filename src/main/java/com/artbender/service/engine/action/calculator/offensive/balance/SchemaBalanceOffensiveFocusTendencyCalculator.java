package com.artbender.service.engine.action.calculator.offensive.balance;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
import com.artbender.model.tendency.OffensiveFocusTendency;
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
@Order(4)
public class SchemaBalanceOffensiveFocusTendencyCalculator implements OffensiveCalculator {

    private final static List<Integer> SCHEMA_BALANCE_PLAYER_SCREEN_SCREEN = List.of(10, 5, 10, 8, 0, 0, 5, 8, 10, 10);
    private final static List<Integer> SCHEMA_BALANCE_PLAYER_SCREEN_BALANCE = List.of(60, 50, 60, 55, 50, 50, 55, 50, 50, 50);
    private final static List<Integer> SCHEMA_BALANCE_PLAYER_SCREEN_OPENING = List.of(10, 5, 10, 8, 0, 0, 5, 8, 10, 10);

    private RandomService randomService;

    @Autowired
    public SchemaBalanceOffensiveFocusTendencyCalculator(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean applicable(Schema schema) {
        return schema == Schema.MID;
    }

    @Override
    public int calculate(Player player) {
        int weight = 0;
        if (player.getPlayerGamePlan().getScreenOpening() == OffensiveFocusTendency.SCREEN) {
            weight += SCHEMA_BALANCE_PLAYER_SCREEN_SCREEN.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getScreenOpening() == OffensiveFocusTendency.BALANCE) {
            weight += SCHEMA_BALANCE_PLAYER_SCREEN_BALANCE.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getScreenOpening() == OffensiveFocusTendency.OPENING) {
            weight += SCHEMA_BALANCE_PLAYER_SCREEN_OPENING.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        }
        return weight;
    }
}
