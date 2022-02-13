package com.artbender.service.engine.action.operation;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Coach;
import com.artbender.model.exceptions.NbaManagerException;
import com.artbender.model.plan.CoachGamePlan;
import com.artbender.model.tendency.ShootingFocusTendency;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.action.support.Schema;
import com.artbender.service.engine.context.GameContext;
import com.artbender.service.engine.random.RandomService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@Slf4j
@Service
@Order(4)
public class SchemaAction implements Action {

    // # 0 - inside, 1 - medium, 2 - outside
    private final static List<Integer> INSIDE_SCHEMA = List.of(0, 0, 0, 1, 0, 0, 1, 0, 1, 2);
    private final static List<Integer> BALANCE_SCHEMA = List.of(0, 1, 1, 1, 2, 1, 1, 0, 1, 2);
    private final static List<Integer> THREE_PT_SCHEMA = List.of(2, 1, 2, 1, 2, 1, 2, 2, 1, 2);

    private RandomService randomService;

    @Autowired
    public SchemaAction(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.SCHEMA;
    }

    @Override
    public void execute(GameContext gameContext) {
        Coach offensiveCoach = gameContext.getActionParams().getOffensiveCoach();
        CoachGamePlan coachGamePlan = offensiveCoach.getCoachGamePlan();
        log.debug("Coach Plan = {} and coach name = {}", coachGamePlan, offensiveCoach.getName());
        List<Integer> currentSchemasList = getCurrentSchemasList(coachGamePlan);
        int schemaValue = currentSchemasList.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        gameContext.getActionGameEventListener().setSchema(getCurrentSchema(schemaValue));
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.ATTACK);
    }

    private List<Integer> getCurrentSchemasList(CoachGamePlan coachGamePlan) {
        if (coachGamePlan.getInsideOutside() == ShootingFocusTendency.INSIDE) {
            return INSIDE_SCHEMA;
        } else if (coachGamePlan.getInsideOutside() == ShootingFocusTendency.BALANCE) {
            return BALANCE_SCHEMA;
        } else if (coachGamePlan.getInsideOutside() == ShootingFocusTendency.THREEPT) {
            return THREE_PT_SCHEMA;
        }
        throw new NbaManagerException("Coach Schema is not found. Please correct it.");
    }

    private Schema getCurrentSchema(int schemaValue) {
        if (schemaValue == 0) {
            return Schema.INSIDE;
        } else if (schemaValue == 1) {
            return Schema.MID;
        } else if (schemaValue == 2) {
            return Schema.THREEPT;
        }
        throw new NbaManagerException("Schema is not found. Check configuration level");
    }
}
