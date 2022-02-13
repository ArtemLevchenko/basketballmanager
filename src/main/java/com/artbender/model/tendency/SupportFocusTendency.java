package com.artbender.model.tendency;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "supportFocusTendency")
@XmlEnum()
public enum SupportFocusTendency {

    REBOUND("REBOUND"), BALANCE("BALANCE"), ASSIST("ASSIST");

    private final String label;

    SupportFocusTendency(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
