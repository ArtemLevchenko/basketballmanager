package com.artbender.service.engine.action.calculator.defensive.outside;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
import com.artbender.model.exceptions.NbaManagerException;
import com.artbender.model.support.Level;
import com.artbender.service.engine.action.calculator.defensive.DefensiveCalculator;
import com.artbender.service.engine.action.support.Schema;
import com.artbender.service.engine.random.RandomService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@Service
@Order(2)
public class DefensiveSchemaOutsideSkillsCalculator implements DefensiveCalculator {

    private final static List<Integer> SKILLS_A = List.of(100, 95, 92, 100, 98, 97, 94, 95, 100, 90);
    private final static List<Integer> SKILLS_B = List.of(85, 87, 83, 82, 80, 85, 87, 83, 86, 89);
    private final static List<Integer> SKILLS_C = List.of(78, 73, 71, 74, 75, 70, 68, 67, 69, 65);
    private final static List<Integer> SKILLS_D = List.of(59, 57, 58, 54, 55, 50, 58, 57, 59, 59);
    private final static List<Integer> SKILLS_E = List.of(49, 47, 48, 44, 45, 40, 44, 47, 48, 49);
    private final static List<Integer> SKILLS_F = List.of(39, 37, 38, 34, 35, 30, 34, 27, 28, 20);

    private RandomService randomService;

    @Autowired
    public DefensiveSchemaOutsideSkillsCalculator(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean applicable(Schema schema) {
        return schema == Schema.THREEPT;
    }

    @Override
    public int calculate(Player player) {
        if (player.getSkills().getPerimeterDefense() == Level.A) {
            return SKILLS_A.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getSkills().getPerimeterDefense() == Level.B) {
            return SKILLS_B.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getSkills().getPerimeterDefense() == Level.C) {
            return SKILLS_C.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getSkills().getPerimeterDefense() == Level.D) {
            return SKILLS_D.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getSkills().getPerimeterDefense() == Level.E) {
            return SKILLS_E.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        } else if (player.getSkills().getPerimeterDefense() == Level.F) {
            return SKILLS_F.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        }
        throw new NbaManagerException("Skills Calculator failed because Skills getPerimeterDefense not founded.");
    }

}
