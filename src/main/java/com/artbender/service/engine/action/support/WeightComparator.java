package com.artbender.service.engine.action.support;

import com.artbender.model.dto.logic.PlayerWeightDTO;

import java.util.Comparator;

public class WeightComparator implements Comparator<PlayerWeightDTO> {

    @Override
    public int compare(PlayerWeightDTO o1, PlayerWeightDTO o2) {
        int returnValue = 0;
        if (o1.getWeightPerAction() > o2.getWeightPerAction()) {
            returnValue = -1;
        } else if (o1.getWeightPerAction() < o2.getWeightPerAction()) {
            returnValue = 1;
        }
        return returnValue;
    }
}
