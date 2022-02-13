package com.artbender.service.orchestrator;

import com.artbender.model.stats.TeamStats;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.clock.GameClock;
import com.artbender.service.engine.context.ActionParams;
import com.artbender.service.engine.context.GameContext;
import com.artbender.service.engine.event.ActionGameEventListener;
import com.artbender.service.engine.message.GameMessageTray;
import com.artbender.service.transformation.loader.CoachLoader;
import com.artbender.service.transformation.loader.PlayerLoader;
import org.springframework.stereotype.Service;

@Service
public class GameInitService {

    private final PlayerLoader playerLoader;
    private final CoachLoader coachLoader;

    public GameInitService(PlayerLoader playerLoader, CoachLoader coachLoader) {
        this.playerLoader = playerLoader;
        this.coachLoader = coachLoader;
    }

    public GameContext initGameContext(String teamHome, String teamAway) {
        return GameContext.builder()
                .actionParams(initActionParams(teamHome, teamAway))
                .currentAction(GameAction.INIT)
                .actionGameEventListener(new ActionGameEventListener())
                .gameClock(new GameClock())
                .gameMessageTray(new GameMessageTray())
                .build();
    }

    private ActionParams initActionParams(String teamHome, String teamAway) {
        return ActionParams.builder()
                .homeCoach(coachLoader.loadData(teamHome).get(0))
                .awayCoach(coachLoader.loadData(teamAway).get(0))
                .homePlayers(playerLoader.loadData(teamHome))
                .awayPlayers(playerLoader.loadData(teamAway))
                .homeStats(new TeamStats(7))
                .awayStats(new TeamStats(7))
                .build();
    }
}
