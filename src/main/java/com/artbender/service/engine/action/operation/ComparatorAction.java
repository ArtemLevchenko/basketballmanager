package com.artbender.service.engine.action.operation;

import com.artbender.model.constants.GameConstants;
import com.artbender.model.db.Player;
import com.artbender.model.dto.logic.PlayerWeightDTO;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.action.support.WeightComparator;
import com.artbender.service.engine.context.GameContext;
import com.artbender.service.engine.random.RandomService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Slf4j
@Service
@Order(7)
public class ComparatorAction implements Action {

    private final Map<Integer, List<Integer>> PLAYER_SIZE_MAP = Map.of(
            1, List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            2, List.of(1, 0, 1, 0, 1, 0, 1, 0, 1, 0),
            3, List.of(1, 0, 1, 0, 1, 0, 2, 0, 2, 0),
            4, List.of(0, 0, 1, 0, 0, 1, 0, 1, 2, 3),
            5, List.of(0, 1, 0, 2, 0, 3, 2, 1, 1, 4));

    private RandomService randomService;

    @Autowired
    public ComparatorAction(RandomService randomService) {
        this.randomService = randomService;
    }

    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.TEAM_COMPARE;
    }

    @Override
    public void execute(GameContext gameContext) {
        List<PlayerWeightDTO> offensivePlayerWeightList = gameContext.getActionGameEventListener().getOffensivePlayerWeightList();
        List<PlayerWeightDTO> defensivePlayerWeightList = gameContext.getActionGameEventListener().getDefensivePlayerWeightList();

        boolean isAttackDominate = compare(offensivePlayerWeightList, defensivePlayerWeightList);
        gameContext.getActionGameEventListener().setAttackDominate(isAttackDominate);

        PlayerWeightDTO playerOffenseWeightDTO = getAttackPlayer(offensivePlayerWeightList);
        log.debug("Selected Off. Player = {}", playerOffenseWeightDTO.getPlayer());
        PlayerWeightDTO playerDefenseWeightDTO = getDefensivePlayerByOffensivePlayer(playerOffenseWeightDTO.getPlayer(), defensivePlayerWeightList);
        log.debug("Selected Def. Player = {}", playerOffenseWeightDTO.getPlayer());

        // remove all and set one by one
        gameContext.getActionGameEventListener().setOffensivePlayerWeightList(List.of(playerOffenseWeightDTO));
        gameContext.getActionGameEventListener().setDefensivePlayerWeightList(List.of(playerDefenseWeightDTO));
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.SHOOT_PROCESS);
    }

    private boolean compare(List<PlayerWeightDTO> offensivePlayers, List<PlayerWeightDTO> defensivePlayers) {
        // result IF OFFENSE > deffense = max 60% attempt shoot
        // result IF deffense > OFFENSE = max 40% attempt shoot
        int offensiveWeight = 0;
        for (PlayerWeightDTO plw : offensivePlayers) {
            offensiveWeight += plw.getWeightPerAction();
        }
        int defensiveWeight = 0;
        for (PlayerWeightDTO plw : defensivePlayers) {
            defensiveWeight += plw.getWeightPerAction();
        }
        return offensiveWeight > defensiveWeight;
    }

    public PlayerWeightDTO getAttackPlayer(List<PlayerWeightDTO> offensivePlayerWeightList) {
        offensivePlayerWeightList.sort(new WeightComparator());
        List<Integer> searchResult = getFocusOffensiveList(offensivePlayerWeightList.size());
        int index = searchResult.get(randomService.randomIntegerIndex(GameConstants.START_FIND, GameConstants.END_FIND));
        return offensivePlayerWeightList.get(index);
    }

    private PlayerWeightDTO getDefensivePlayerByOffensivePlayer(Player offensivePlayer, List<PlayerWeightDTO> defensivePlayerWeightList) {
        defensivePlayerWeightList.sort(new WeightComparator());
        return defensivePlayerWeightList.stream().filter(playerWeightDTO -> playerWeightDTO.getPlayer().getCurrentGamePosition() == offensivePlayer.getCurrentGamePosition()).findFirst().orElse(defensivePlayerWeightList.get(0));
    }

    private List<Integer> getFocusOffensiveList(int size) {
        return PLAYER_SIZE_MAP.get(size);
    }
}
