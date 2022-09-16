package com.artbender.service.engine.action.calculator.substitution;

import com.artbender.model.db.Player;
import com.artbender.model.dto.logic.PlayerWeightDTO;
import com.artbender.model.support.GamePosition;
import com.artbender.model.support.GameRole;
import com.artbender.model.tendency.SubstitutionRating;
import com.artbender.service.engine.action.support.WeightComparator;
import com.artbender.service.engine.clock.GameClock;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@NoArgsConstructor
@Service
public class SubstitutionService {

    public void execute(List<Player> fullRosterList, SubstitutionRating substitutionRating, GameClock gameClock) {
        List<Player> needToBenchPlayerList = fullRosterList
                .stream()
                .filter(player -> player.isInStart() && this.hasNeedToSubstitution(player, substitutionRating.getLabel(), gameClock))
                .toList();
        if (needToBenchPlayerList.isEmpty()) {
            return;
        }
        // player id -> inStart bool
        Map<Integer, Boolean> substitutionMap = new HashMap<>();
        List<PlayerWeightDTO> notInStartWeightPlayerList = this.calculateAndRangeBenchPlayerByCriteria(fullRosterList);
        for (Player tiredPlayer : needToBenchPlayerList) {
            // Algo: position = (G -> G or F), F - (G or F or C), C - (C or F) + rating + stamina = 80% of full + stats
            List<GamePosition> subsPositions = this.getSubstitutionPositions(tiredPlayer.getCurrentGamePosition());
            PlayerWeightDTO newActivePlayerWeightDTO = notInStartWeightPlayerList.stream().filter(playerWeightDTO ->
                            subsPositions.contains(playerWeightDTO.getPlayer().getCurrentGamePosition()))
                    .findFirst()
                    .orElse(null);
            // remove from the list
            notInStartWeightPlayerList.remove(newActivePlayerWeightDTO);
            if (newActivePlayerWeightDTO != null) {
                substitutionMap.put(tiredPlayer.getId(), false);
                substitutionMap.put(newActivePlayerWeightDTO.getPlayer().getId(), true);
            }
        }
        for (Player player : fullRosterList) { // update data
            int playerId = player.getId();
            if (substitutionMap.containsKey(playerId)) {
                player.setInStart(substitutionMap.get(playerId));
            }
        }
    }

    private boolean hasNeedToSubstitution(Player player, int substitutionRating, GameClock gameClock) {
        double currentStamina = player.getCharacteristics().getCurrentStamina();
        double fullStamina = player.getCharacteristics().getStamina();
        return (gameClock.getPeriod() >= 4 && gameClock.getMinutes() <= 4
                && (player.getGameRole() == GameRole.BENCH_PLAYER ||
                player.getGameRole() == GameRole.ROLE_PLAYER ||
                player.getGameRole() == GameRole.SIX_MAN)) || currentStamina * 100 / fullStamina <= substitutionRating;

    }

    private List<PlayerWeightDTO> calculateAndRangeBenchPlayerByCriteria(List<Player> fullRosterList) {
        List<Player> notInStartPlayerList = this.getNotInStartPlayers(fullRosterList);
        List<PlayerWeightDTO> notInStartPlayerWeightDTOs = new ArrayList<>();
        for (Player player : notInStartPlayerList) {
            int weight = player.getCharacteristics().getTotalRank() / 10;
            double currentStamina = player.getCharacteristics().getCurrentStamina();
            double fullStamina = player.getCharacteristics().getStamina();
            weight += currentStamina * 100 / fullStamina >= 80 ? 8 : 2;
            weight += player.getPlayerStats().get_PTS();
            weight += player.getPlayerStats().get_DEFR();
            weight += player.getPlayerStats().get_OFFR();
            weight += player.getPlayerStats().get_AST();
            weight += player.getPlayerStats().getMinutes() / 10;
            weight += this.getWeightByGameRole(player.getGameRole());
            notInStartPlayerWeightDTOs.add(new PlayerWeightDTO(weight, player));
        }
        notInStartPlayerWeightDTOs.sort(new WeightComparator());
        return notInStartPlayerWeightDTOs;
    }


    private List<Player> getNotInStartPlayers(List<Player> fullRosterList) {
        return fullRosterList.stream().filter(player -> !player.isInStart()).toList();
    }

    private List<GamePosition> getSubstitutionPositions(GamePosition currentGamePosition) {
        return switch (currentGamePosition) {
            case G -> Arrays.asList(GamePosition.G, GamePosition.F);
            case F -> Arrays.asList(GamePosition.G, GamePosition.F, GamePosition.C);
            case C -> Arrays.asList(GamePosition.C, GamePosition.F);
        };
    }

    private int getWeightByGameRole(GameRole gameRole) {
        return switch (gameRole) {
            case HERO -> 20;
            case START_PLAYER -> 12;
            case SIX_MAN -> 8;
            case ROLE_PLAYER -> 5;
            case BENCH_PLAYER -> 1;
        };
    }
}
