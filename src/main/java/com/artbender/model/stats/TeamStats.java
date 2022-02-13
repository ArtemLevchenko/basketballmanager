package com.artbender.model.stats;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
public class TeamStats implements Serializable {
    private int _FGM;
    private int _FGA;
    private int _3PM;
    private int _3PA;
    private int _FTM;
    private int _FTA;
    private int _OFFR;
    private int _DEFR;
    private int _TOTR;
    private int _AST;
    private int _PF;
    private int _ST;
    private int _TO;
    private int _BS;
    private int _PTS;
    private int timeouts;

    public TeamStats(int timeouts) {
        this.timeouts = timeouts;
    }
}
