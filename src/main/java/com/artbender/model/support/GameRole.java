package com.artbender.model.support;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "gameRole")
@XmlEnum()
public enum GameRole {
    HERO("HERO"), START_PLAYER("START_PLAYER"), SIX_MAN("SIX_MAN"), ROLE_PLAYER("ROLE_PLAYER"), BENCH_PLAYER ("BENCH_PLAYER");
    private final String label;

     GameRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
