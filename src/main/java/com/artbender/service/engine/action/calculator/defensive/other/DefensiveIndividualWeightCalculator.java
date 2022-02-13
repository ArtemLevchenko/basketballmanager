package com.artbender.service.engine.action.calculator.defensive.other;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Coach;
import com.artbender.model.db.Player;
import com.artbender.model.exceptions.NbaManagerException;
import com.artbender.model.support.Level;
import com.artbender.model.tendency.GameFocusTendency;
import com.artbender.service.engine.random.RandomService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@Service
public class DefensiveIndividualWeightCalculator {

    private final static List<Integer> OFFENSIVE_COACH_FOCUS = List.of(0, 5, 0, 5, 10, 5, 5, 10, 0, 10);
    private final static List<Integer> BALANCE_COACH_FOCUS = List.of(85, 85, 80, 85, 80, 85, 85, 75, 80, 75);
    private final static List<Integer> DEFENSIVE_COACH_FOCUS = List.of(130, 120, 130, 125, 120, 125, 125, 100, 110, 110);

    private final static List<Integer> SKILLS_A = List.of(100, 95, 92, 100, 98, 97, 94, 95, 100, 90);
    private final static List<Integer> SKILLS_B = List.of(85, 87, 83, 82, 80, 85, 87, 83, 86, 89);
    private final static List<Integer> SKILLS_C = List.of(78, 73, 71, 74, 75, 70, 68, 67, 69, 65);
    private final static List<Integer> SKILLS_D = List.of(59, 57, 58, 54, 55, 50, 58, 57, 59, 59);
    private final static List<Integer> SKILLS_E = List.of(49, 47, 48, 44, 45, 40, 44, 47, 48, 49);
    private final static List<Integer> SKILLS_F = List.of(39, 37, 38, 34, 35, 30, 34, 27, 28, 20);

    private final static List<Integer> OFFENSIVE_PLAYER_FOCUS = List.of(20, 15, 20, 18, 32, 35, 15, 28, 20, 22);
    private final static List<Integer> BALANCE_PLAYER_FOCUS = List.of(60, 60, 65, 55, 45, 50, 50, 60, 65, 45);
    private final static List<Integer> DEFENSIVE_PLAYER_FOCUS = List.of(120, 120, 100, 90, 110, 100, 120, 90, 80, 90);

    private RandomService randomService;

    @Autowired
    public DefensiveIndividualWeightCalculator(RandomService randomService) {
        this.randomService = randomService;
    }

    public int calculate(Coach coach, Player player) {
        int coachWeight = getCoachWeight(coach);
        int playerIqWeight = getPlayerIqWeight(player);
        int defensivePlayerWeight = getDefensiveWeight(player);
        int weight = defensivePlayerWeight + playerIqWeight +
                ((player.getCharacteristics().getDefenseOnBall() + player.getCharacteristics().getTotalRank() + coachWeight) / 2);
        if (player.getCharacteristics().getCurrentStamina() <= 0) {
            weight = weight - 80;
        }
        return Math.max(weight, 0);
    }

    private int getDefensiveWeight(Player player) {
        if (player.getPlayerGamePlan().getOffenseDefense() == GameFocusTendency.OFFENSIVE) {
            return OFFENSIVE_PLAYER_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getOffenseDefense() == GameFocusTendency.BALANCE) {
            return BALANCE_PLAYER_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getPlayerGamePlan().getOffenseDefense() == GameFocusTendency.DEFENSIVE) {
            return DEFENSIVE_PLAYER_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        }
        throw new NbaManagerException("Action: Defensive. Coach Focus is not found!");
    }


    private int getCoachWeight(Coach coach) {
        if (coach.getCoachGamePlan().getOffenseDefense() == GameFocusTendency.OFFENSIVE) {
            return OFFENSIVE_COACH_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (coach.getCoachGamePlan().getOffenseDefense() == GameFocusTendency.BALANCE) {
            return BALANCE_COACH_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (coach.getCoachGamePlan().getOffenseDefense() == GameFocusTendency.DEFENSIVE) {
            return DEFENSIVE_COACH_FOCUS.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        }
        throw new NbaManagerException("Action: Defensive. Coach Focus is not found!");
    }

    private int getPlayerIqWeight(Player player) {
        if (player.getSkills().getIq() == Level.A) {
            return SKILLS_A.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getSkills().getIq() == Level.B) {
            return SKILLS_B.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getSkills().getIq() == Level.C) {
            return SKILLS_C.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getSkills().getIq() == Level.D) {
            return SKILLS_D.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getSkills().getIq() == Level.E) {
            return SKILLS_E.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getSkills().getIq() == Level.F) {
            return SKILLS_F.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        }
        throw new NbaManagerException("Action: Defensive. Player IQ is not found!");
    }
}
