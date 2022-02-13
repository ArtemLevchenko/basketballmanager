package com.artbender.service.engine.action.calculator.offensive.balance;

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
@Order(3)
public class SchemaBalanceShootingFocusTendencyCalculator implements OffensiveCalculator {

    private final static List<Integer> SCHEMA_BALANCE_PLAYER_INSIDE = List.of(25, 20, 20, 25, 20, 20, 25, 30, 20, 20);
    private final static List<Integer> SCHEMA_BALANCE_PLAYER_BALANCE = List.of(80, 70, 60, 75, 80, 80, 65, 70, 60, 90);
    private final static List<Integer> SCHEMA_BALANCE_PLAYER_THREE_PT = List.of(35, 30, 40, 35, 30, 40, 35, 30, 40, 40);

    private RandomService randomService;

    @Autowired
    public SchemaBalanceShootingFocusTendencyCalculator(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean applicable(Schema schema) {
        return schema == Schema.MID;
    }

    @Override
    public int calculate(Player player) {
        int weight = player.getCharacteristics().getMediumShot();
        if (player.getPlayerGamePlan().getInsideOutside() == ShootingFocusTendency.INSIDE) {
            weight += SCHEMA_BALANCE_PLAYER_INSIDE.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getInsideOutside() == ShootingFocusTendency.BALANCE) {
            weight += SCHEMA_BALANCE_PLAYER_BALANCE.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getInsideOutside() == ShootingFocusTendency.THREEPT) {
            weight += SCHEMA_BALANCE_PLAYER_THREE_PT.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        }
        return weight;
    }
}
