package com.artbender.model.stats;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class PlayerStats implements Serializable {

    private int minutes;
    private int seconds;
    private int _FGM;
    private int _FGA;
    private int _3PM;
    private int _3PA;
    private int _FTM;
    private int _FTA;
    private int _OFFR;
    private int _DEFR;
    private int _AST;
    private int _PF;
    private int _ST;
    private int _TO;
    private int _BS;
    private int _PTS;

    public void setSeconds(int seconds) {
        int newSeconds = seconds + this.seconds;
        if (newSeconds > 60) {
            this.seconds = newSeconds - 60;
            minutes++;
        } else {
            this.seconds = newSeconds;
        }
    }

    public String getFormatMinutes() {
        if (minutes < 10) {
            return "0" + minutes;
        }
        return String.valueOf(minutes);
    }

    public String getFormatSeconds() {
        if (seconds < 10) {
            return "0" + seconds;
        }
        return String.valueOf(seconds);
    }
}
