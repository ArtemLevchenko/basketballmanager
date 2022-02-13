package com.artbender.service.engine.action.calculator.offensive.outside;

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
@Order(7)
public class SchemaThreeptOffensiveFocusTendencyCalculator implements OffensiveCalculator {

    private final static List<Integer> SCHEMA_THREE_PT_PLAYER_SCREEN_SCREEN = List.of(10, 5, 10, 8, 12, 15, 5, 8, 10, 12);
    private final static List<Integer> SCHEMA_THREE_PT_PLAYER_SCREEN_BALANCE = List.of(50, 50, 55, 45, 35, 30, 40, 50, 45, 35);
    private final static List<Integer> SCHEMA_THREE_PT_PLAYER_SCREEN_OPENING = List.of(100, 100, 90, 80, 100, 90, 100, 70, 80, 90);

    private RandomService randomService;

    @Autowired
    public SchemaThreeptOffensiveFocusTendencyCalculator(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean applicable(Schema schema) {
        return schema == Schema.THREEPT;
    }

    @Override
    public int calculate(Player player) {
        int weight = 0;
        if (player.getPlayerGamePlan().getScreenOpening() == OffensiveFocusTendency.SCREEN) {
            weight += SCHEMA_THREE_PT_PLAYER_SCREEN_SCREEN.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getScreenOpening() == OffensiveFocusTendency.BALANCE) {
            weight += SCHEMA_THREE_PT_PLAYER_SCREEN_BALANCE.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getScreenOpening() == OffensiveFocusTendency.OPENING) {
            weight += SCHEMA_THREE_PT_PLAYER_SCREEN_OPENING.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        }
        return weight;
    }

}
