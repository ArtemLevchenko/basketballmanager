package com.artbender.model.tendency;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "offensiveFocusTendency")
@XmlEnum()
public enum OffensiveFocusTendency {

    SCREEN("SCREEN"), BALANCE("BALANCE"), OPENING("OPENING");

    private final String label;

    OffensiveFocusTendency(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
