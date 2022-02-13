package com.artbender.service.engine.message;

import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class GameMessageTray {

    private List<String> poolMessages;
    private String activeMessage;

    public GameMessageTray() {
        this.poolMessages = new ArrayList<>();
        this.activeMessage = null;
    }
}
