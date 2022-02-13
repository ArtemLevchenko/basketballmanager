package com.artbender.model.dto.logic;

import com.artbender.model.db.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerWeightDTO {
    private int weightPerAction;
    private Player player;
}
