package com.artbender.service.engine.action.support;

public enum Schema {
    INSIDE("INSIDE"), MID("MID"), THREEPT("THREEPT");

    private final String label;

    Schema(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
