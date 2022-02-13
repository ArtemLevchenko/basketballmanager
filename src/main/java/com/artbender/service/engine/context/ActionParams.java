package com.artbender.service.engine.context;

import com.artbender.model.db.Coach;
import com.artbender.model.db.Player;
import com.artbender.model.stats.TeamStats;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(builderClassName = "Builder")
public class ActionParams {

    private List<Player> homePlayers;
    private Coach homeCoach;

    private List<Player> awayPlayers;
    private Coach awayCoach;

    private List<Player> offensivePlayers;
    private Coach offensiveCoach;

    private List<Player> defensivePlayers;
    private Coach defensiveCoach;

    private TeamStats homeStats;
    private TeamStats awayStats;
}
