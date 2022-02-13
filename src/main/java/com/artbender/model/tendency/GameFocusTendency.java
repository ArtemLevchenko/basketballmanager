package com.artbender.model.tendency;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "gameFocusTendency")
@XmlEnum()
public enum GameFocusTendency {

    OFFENSIVE("OFFENSIVE"), BALANCE("BALANCE"), DEFENSIVE("DEFENSIVE");

    private final String label;

    GameFocusTendency(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
