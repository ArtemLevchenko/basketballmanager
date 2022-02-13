package com.artbender.service.engine.action.calculator.offensive.inside;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
import com.artbender.model.tendency.ShootingFocusTendency;
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
@Order(0)
public class SchemaInsideShootingFocusTendencyCalculator implements OffensiveCalculator {

    private final static List<Integer> SCHEMA_INSIDE_PLAYER_INSIDE = List.of(80, 70, 60, 75, 80, 80, 65, 70, 60, 90);
    private final static List<Integer> SCHEMA_INSIDE_PLAYER_BALANCE = List.of(50, 50, 55, 45, 35, 30, 40, 50, 45, 35);
    private final static List<Integer> SCHEMA_INSIDE_PLAYER_THREE_PT = List.of(10, 5, 10, 8, 12, 15, 5, 8, 10, 12);

    private RandomService randomService;

    @Autowired
    public SchemaInsideShootingFocusTendencyCalculator(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean applicable(Schema schema) {
        return schema == Schema.INSIDE;
    }

    @Override
    public int calculate(Player player) {
        int weight = Math.max(player.getCharacteristics().getInsideScore(), player.getCharacteristics().getDunk());
        if (player.getPlayerGamePlan().getInsideOutside() == ShootingFocusTendency.INSIDE) {
            weight += SCHEMA_INSIDE_PLAYER_INSIDE.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getInsideOutside() == ShootingFocusTendency.BALANCE) {
            weight += SCHEMA_INSIDE_PLAYER_BALANCE.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getInsideOutside() == ShootingFocusTendency.THREEPT) {
            weight += SCHEMA_INSIDE_PLAYER_THREE_PT.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        }
        return weight;
    }
}
