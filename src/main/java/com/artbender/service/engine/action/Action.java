package com.artbender.service.engine.action;

import com.artbender.service.engine.context.GameContext;

public interface Action {
    boolean isValidPreExecuteAction(GameContext gameContext);
    void execute(GameContext gameContext);
    void postExecuteAction(GameContext gameContext);
}
