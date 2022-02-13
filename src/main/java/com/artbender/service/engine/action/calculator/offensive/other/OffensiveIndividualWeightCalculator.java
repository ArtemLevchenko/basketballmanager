package com.artbender.service.engine.action.calculator.offensive.other;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Coach;
import com.artbender.model.db.Player;
import com.artbender.model.tendency.GameFocusTendency;
import com.artbender.service.engine.random.RandomService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@Service
public class OffensiveIndividualWeightCalculator {

    private final static List<Integer> OFFENSIVE_FOCUS = List.of(50, 45, 45, 50, 35, 40, 50, 45, 45, 40);
    private final static List<Integer> BALANCE_FOCUS = List.of(25, 25, 30, 35, 20, 25, 25, 25, 20, 25);
    private final static List<Integer> DEFENSIVE_FOCUS = List.of(0, 10, 0, 15, 10, 5, 5, 10, 0, 10);

    private RandomService randomService;

    @Autowired
    public OffensiveIndividualWeightCalculator(RandomService randomService) {
        this.randomService = randomService;
    }

    public int calculate(Coach coach, Player player) {
        int coachWeight = 0;
        if (coach.getCoachGamePlan().getOffenseDefense() == GameFocusTendency.OFFENSIVE) {
            coachWeight = OFFENSIVE_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (coach.getCoachGamePlan().getOffenseDefense() == GameFocusTendency.BALANCE) {
            coachWeight = BALANCE_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (coach.getCoachGamePlan().getOffenseDefense() == GameFocusTendency.DEFENSIVE) {
            coachWeight = DEFENSIVE_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        }
        int weight = (player.getCharacteristics().getBallSecurity() + player.getCharacteristics().getTotalRank() + coachWeight) / 2;
        if (player.getCharacteristics().getCurrentStamina() <= 0) {
            weight = weight - 100;
        }
        return Math.max(weight, 0);
    }
}
