package com.artbender.service.engine.context;

import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.clock.GameClock;
import com.artbender.service.engine.event.ActionGameEventListener;
import com.artbender.service.engine.message.GameMessageTray;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder(builderClassName = "Builder")
public class GameContext {

    private ActionParams actionParams;
    private GameAction currentAction;
    private GameClock gameClock;
    private GameMessageTray gameMessageTray;
    private ActionGameEventListener actionGameEventListener;


}
