package com.artbender.service.engine.action.calculator.defensive;

import com.artbender.model.db.Player;
import com.artbender.service.engine.action.support.Schema;

public interface DefensiveCalculator {
    boolean applicable(Schema schema);
    int calculate(Player player);
}
