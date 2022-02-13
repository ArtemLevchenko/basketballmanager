package com.artbender.service.engine.action.operation;

import com.artbender.model.db.Player;
import com.artbender.model.stats.PlayerStats;
import com.artbender.model.stats.TeamStats;
import com.artbender.service.engine.action.Action;
import com.artbender.service.engine.action.support.GameAction;
import com.artbender.service.engine.action.support.Schema;
import com.artbender.service.engine.context.GameContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Slf4j
@Service
@Order(15)
public class UpdateStatsAndFatigueAction implements Action {
    @Override
    public boolean isValidPreExecuteAction(GameContext gameContext) {
        return gameContext.getCurrentAction() == GameAction.UPDATE_STATS;
    }

    @Override
    public void execute(GameContext gameContext) {
        Map<Player, List<GameAction>> statsMaps = gameContext.getActionGameEventListener().getPlayerActionsMap();
        List<Player> playersAway = gameContext.getActionParams().getAwayPlayers();
        List<Player> playersHome = gameContext.getActionParams().getHomePlayers();

        Integer timeRangePerAttack = gameContext.getGameClock().getRangeStep();
        update(gameContext, playersAway, statsMaps, timeRangePerAttack);
        update(gameContext, playersHome, statsMaps, timeRangePerAttack);
        //update onBench
        updateFatigueOnBench(playersHome);
        updateFatigueOnBench(playersAway);

        gameContext.getActionParams().setHomeStats(updateTeamStats(playersHome, gameContext.getActionParams().getHomeStats().getTimeouts()));
        gameContext.getActionParams().setAwayStats(updateTeamStats(playersAway, gameContext.getActionParams().getAwayStats().getTimeouts()));
    }

    @Override
    public void postExecuteAction(GameContext gameContext) {
        gameContext.setCurrentAction(GameAction.RESULT);
    }

    private void update(GameContext gameContext, List<Player> players, Map<Player, List<GameAction>> statsMaps, Integer range) {
        for (Player player : players) {
            if (player.isInStart()) {
                player.getPlayerStats().setSeconds(range);
                // update fatigue
                player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() - 1);
                if (statsMaps.containsKey(player)) {
                    for (GameAction action : statsMaps.get(player)) {
                        PlayerStats playerStats = player.getPlayerStats();
                        if (action == GameAction.SCORE) {
                            updateScoreStats(player, gameContext.getActionGameEventListener().getScore());
                        } else if (action == GameAction.FREETHROW) {
                            updateFTAScoreStats(player, gameContext.getActionGameEventListener().getScore(),
                                    gameContext.getActionGameEventListener().getSchema());
                        } else if (action == GameAction.MISS) {
                            updateMissStats(player, gameContext.getActionGameEventListener().getSchema());
                        } else if (action == GameAction.TURNOVER) {
                            playerStats.set_TO(playerStats.get_TO() + 1);
                            player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() - 2);
                        } else if (action == GameAction.BLOCK) {
                            playerStats.set_BS(playerStats.get_BS() + 1);
                            player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() - 1);
                        } else if (action == GameAction.STEAL) {
                            playerStats.set_ST(playerStats.get_ST() + 1);
                            player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() - 1);
                        } else if (action == GameAction.ASSIST) {
                            playerStats.set_AST(playerStats.get_AST() + 1);
                            player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() - 1);
                        } else if (action == GameAction.FOUL) {
                            playerStats.set_PF(playerStats.get_PF() + 1);
                            player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() + 1);
                        } else if (action == GameAction.OFREBOUND) {
                            playerStats.set_OFFR(playerStats.get_OFFR() + 1);
                            player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() - 1);
                        } else if (action == GameAction.DREBOUND) {
                            playerStats.set_DEFR(playerStats.get_DEFR() + 1);
                            player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() - 1);
                        }
                    }
                }

            }
        }
    }


    private TeamStats updateTeamStats(List<Player> players, int timeouts) {
        TeamStats teamStats = new TeamStats();
        teamStats.setTimeouts(timeouts);
        for (Player pl : players) {
            PlayerStats playerStats = pl.getPlayerStats();
            teamStats.set_FGM(teamStats.get_FGM() + playerStats.get_FGM());
            teamStats.set_FGA(teamStats.get_FGA() + playerStats.get_FGA());
            teamStats.set_3PM(teamStats.get_3PM() + playerStats.get_3PM());
            teamStats.set_3PA(teamStats.get_3PA() + playerStats.get_3PA());
            teamStats.set_FTM(teamStats.get_FTM() + playerStats.get_FTM());
            teamStats.set_FTA(teamStats.get_FTA() + playerStats.get_FTA());
            teamStats.set_OFFR(teamStats.get_OFFR() + playerStats.get_OFFR());
            teamStats.set_DEFR(teamStats.get_DEFR() + playerStats.get_DEFR());
            teamStats.set_TOTR(teamStats.get_TOTR() + (playerStats.get_OFFR() + playerStats.get_DEFR()));
            teamStats.set_AST(teamStats.get_AST() + playerStats.get_AST());
            teamStats.set_PF(teamStats.get_PF() + playerStats.get_PF());
            teamStats.set_ST(teamStats.get_ST() + playerStats.get_ST());
            teamStats.set_TO(teamStats.get_TO() + playerStats.get_TO());
            teamStats.set_BS(teamStats.get_BS() + playerStats.get_BS());
            teamStats.set_PTS(teamStats.get_PTS() + playerStats.get_PTS());
        }
        return teamStats;
    }

    private void updateFatigueOnBench(List<Player> players) {
        for (Player pl : players) {
            if (!pl.isInStart()) {
                int fullStamina = pl.getCharacteristics().getStamina();
                int currStamina = pl.getCharacteristics().getCurrentStamina();
                if (currStamina + 4 >= fullStamina) {
                    pl.getCharacteristics().setCurrentStamina(fullStamina);
                } else {
                    pl.getCharacteristics().setCurrentStamina(pl.getCharacteristics().getCurrentStamina() + 4);
                }
            }
        }
    }


    private void updateScoreStats(Player player, int score) {
        PlayerStats playerStats = player.getPlayerStats();
        if (score == 3) {
            playerStats.set_3PA(playerStats.get_3PA() + 1);
            playerStats.set_3PM(playerStats.get_3PM() + 1);
        }
        playerStats.set_FGM(playerStats.get_FGM() + 1);
        playerStats.set_FGA(playerStats.get_FGA() + 1);
        playerStats.set_PTS(playerStats.get_PTS() + score);
        player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() - 3);
    }

    private void updateFTAScoreStats(Player player, int score, Schema typeAttack) {
        PlayerStats playerStats = player.getPlayerStats();
        int attempt = typeAttack == Schema.THREEPT ? 3 : 2;
        playerStats.set_FTA(playerStats.get_FTA() + attempt);
        playerStats.set_FTM(playerStats.get_FTM() + score);
        playerStats.set_PTS(playerStats.get_PTS() + score);
        player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() - 2);
    }

    private void updateMissStats(Player player, Schema schema) {
        PlayerStats playerStats = player.getPlayerStats();
        if (schema == Schema.MID || schema == Schema.INSIDE) {
            playerStats.set_FGA(playerStats.get_FGA() + 1);
        } else if (schema == Schema.THREEPT) {
            playerStats.set_FGA(playerStats.get_FGA() + 1);
            playerStats.set_3PA(playerStats.get_3PA() + 1);
        }
        player.getCharacteristics().setCurrentStamina(player.getCharacteristics().getCurrentStamina() - 3);
    }

}
