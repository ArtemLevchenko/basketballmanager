package com.artbender.model.tendency;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "shootingFocusTendency")
@XmlEnum()
public enum ShootingFocusTendency {
    INSIDE("INSIDE"), BALANCE("BALANCE"), THREEPT("THREEPT");

    private final String label;

    ShootingFocusTendency(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
