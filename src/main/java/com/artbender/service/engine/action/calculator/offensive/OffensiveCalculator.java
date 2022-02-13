package com.artbender.service.engine.action.calculator.offensive;

import com.artbender.model.db.Player;
import com.artbender.service.engine.action.support.Schema;

public interface OffensiveCalculator {
    boolean applicable(Schema schema);
    int calculate(Player player);
}
