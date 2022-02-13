package com.artbender.service.engine.clock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameClock {

    private static final Integer PERIOD_TIME = 12;
    private static final Integer PERIOD_TIME_SECONDS = 0;
    private static final Integer START_PERIOD = 1;

    private int minutes;
    private int seconds;
    private int period;
    private int rangeStep;
    // indicator of started
    private boolean started;
    // indicator of finished game
    private boolean finished;

    public GameClock() {
        this.minutes = PERIOD_TIME;
        this.seconds = PERIOD_TIME_SECONDS;
        this.period = START_PERIOD;
        this.started = Boolean.FALSE;
        this.finished = Boolean.FALSE;
    }


    public void setSeconds(int seconds) {
        int newSeconds = this.seconds - seconds;
        if (newSeconds < 0) {
            this.seconds = 60 - Math.abs(newSeconds);
            minutes--;
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

    public void setOvertime() {
        this.minutes = 5;
        this.seconds = 0;
    }

    public void setQuarter() {
        this.minutes = 12;
        this.seconds = 0;
    }
}
