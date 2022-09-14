package com.artbender.service.transformation.rest;

import com.artbender.model.db.Coach;
import com.artbender.model.db.Player;
import com.artbender.model.dto.rest.BoxScoresPlayerDTO;
import com.artbender.model.dto.rest.BoxScoresTeamDTO;
import com.artbender.model.dto.rest.CoachSettingDTO;
import com.artbender.model.dto.rest.GameEventDTO;
import com.artbender.model.stats.TeamStats;
import com.artbender.service.engine.action.support.OnBall;
import com.artbender.service.engine.clock.GameClock;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Service
public class GameTransformDataService {

    public List<BoxScoresPlayerDTO> buildPlayerResponseDto(List<Player> players) {
        List<BoxScoresPlayerDTO> responseDTOList = new ArrayList<>();
        players.sort(Comparator.comparing(Player::isInStart, Comparator.reverseOrder()));

        players.forEach(player ->
                responseDTOList.add(BoxScoresPlayerDTO.builder()
                        .id(player.getId())
                        .name(player.getName())
                        .inStart(player.isInStart())
                        .currentGamePosition(player.getCurrentGamePosition().name())
                        .stamina((getCurrentStamina(player.getCharacteristics().getCurrentStamina()) * 100) / player.getCharacteristics().getStamina() + "%")
                        .minutes(player.getPlayerStats().getFormatMinutes() + ":" + player.getPlayerStats().getFormatSeconds())
                        ._FG(player.getPlayerStats().get_FGM() + "/" + player.getPlayerStats().get_FGA())
                        ._3P(player.getPlayerStats().get_3PM() + "/" + player.getPlayerStats().get_3PA())
                        ._FT(player.getPlayerStats().get_FTM() + "/" + player.getPlayerStats().get_FTA())
                        ._REB(player.getPlayerStats().get_DEFR() + player.getPlayerStats().get_OFFR())
                        ._DEFR(player.getPlayerStats().get_DEFR())
                        ._OFFR(player.getPlayerStats().get_OFFR())
                        ._AST(player.getPlayerStats().get_AST())
                        ._PF(player.getPlayerStats().get_PF())
                        ._ST(player.getPlayerStats().get_ST())
                        ._BS(player.getPlayerStats().get_BS())
                        ._TO(player.getPlayerStats().get_TO())
                        ._PTS(player.getPlayerStats().get_PTS())
                        .build())
        );
        return responseDTOList;
    }

    private int getCurrentStamina(int stamina) {
        return Math.max(stamina, 0);
    }

    public BoxScoresTeamDTO buildTeamResponseDto(String teamName, TeamStats teamStats) {
        return BoxScoresTeamDTO.builder()
                .teamName(teamName)
                ._FG(teamStats.get_FGM() + "/" + teamStats.get_FGA())
                ._3P(teamStats.get_3PM() + "/" + teamStats.get_3PA())
                ._FT(teamStats.get_FTM() + "/" + teamStats.get_FTA())
                ._REB(teamStats.get_DEFR() + teamStats.get_OFFR())
                ._OFFR(teamStats.get_OFFR())
                ._AST(teamStats.get_AST())
                ._PF(teamStats.get_PF())
                ._ST(teamStats.get_ST())
                ._BS(teamStats.get_BS())
                ._TO(teamStats.get_TO())
                ._PTS(teamStats.get_PTS())
                .timeouts(teamStats.getTimeouts())
                .build();
    }


    public GameEventDTO buildGameEvent(GameClock gameClock, OnBall onBall, String lastEvent) {
        return GameEventDTO.builder()
                .quarter(gameClock.getPeriod())
                .time(gameClock.getFormatMinutes() + ":" + gameClock.getFormatSeconds())
                .onBall(onBall.name())
                .lastEvent(lastEvent)
                .build();
    }


    public CoachSettingDTO buildCoachInfo(Coach coach) {
        return CoachSettingDTO.builder()
                .id(coach.getId())
                .name(coach.getName())
                .offenseDefense(coach.getCoachGamePlan().getOffenseDefense().getLabel())
                .insideOutside(coach.getCoachGamePlan().getInsideOutside().getLabel())
                .substitutionRating(coach.getCoachGamePlan().getSubstitutionRating().getLabel())
                .build();
    }
}
