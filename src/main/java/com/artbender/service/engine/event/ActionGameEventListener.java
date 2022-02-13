package com.artbender.service.engine.event;

import com.artbender.model.db.Player;
import com.artbender.model.dto.logic.PlayerWeightDTO;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.action.support.OnBall;
import com.artbender.service.engine.action.support.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class ActionGameEventListener {

    private int score;
    private Map<Player, List<GameAction>> playerActionsMap;
    private OnBall onBall;
    private Schema schema;
    private List<PlayerWeightDTO> offensivePlayerWeightList;
    private List<PlayerWeightDTO> defensivePlayerWeightList;
    private Boolean attackDominate;

    public ActionGameEventListener() {
        this.score = 0;
        this.playerActionsMap = new HashMap<>();
        this.onBall = OnBall.HOME;
        this.schema = null;
        this.offensivePlayerWeightList = new ArrayList<>();
        this.defensivePlayerWeightList = new ArrayList<>();
        this.attackDominate = Boolean.FALSE;
    }

    public ActionGameEventListener(OnBall onBall) {
        this.onBall = onBall;
        this.score = 0;
        this.playerActionsMap = new HashMap<>();
        this.schema = null;
        this.offensivePlayerWeightList = new ArrayList<>();
        this.defensivePlayerWeightList = new ArrayList<>();
        this.attackDominate = Boolean.FALSE;
    }

    public void putActions(Player pl, GameAction action) {
        if (playerActionsMap.get(pl) != null) {
            playerActionsMap.get(pl).add(action);
        } else {
            List<GameAction> actions = new ArrayList<>();
            actions.add(action);
            playerActionsMap.put(pl, actions);
        }
    }
}
