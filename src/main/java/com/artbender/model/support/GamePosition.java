package com.artbender.model.support;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "gamePosition")
@XmlEnum()
public enum GamePosition {
    G("G"), F("F"), C("C");

    private final String label;

    private GamePosition(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
