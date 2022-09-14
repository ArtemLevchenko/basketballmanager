package com.artbender.model.db;

import com.artbender.model.attribute.Characteristics;
import com.artbender.model.attribute.Skills;
import com.artbender.model.plan.PlayerGamePlan;
import com.artbender.model.stats.PlayerStats;
import com.artbender.model.support.GamePosition;
import com.artbender.model.support.GameRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Player implements Serializable {

    @XmlElement
    private int id;
    @XmlElement
    private String name;
    @XmlTransient
    private PlayerStats playerStats = new PlayerStats();
    @XmlElement
    private Team team;
    @XmlElement
    private GamePosition currentGamePosition;
    @XmlElement
    private Characteristics characteristics;
    @XmlElement
    private Skills skills;
    @XmlElement
    private PlayerGamePlan playerGamePlan;
    @XmlElement
    private boolean inStart;
    @XmlElement
    private GameRole gameRole;

    //TODO: Add mental characteristics -> Confidence, form etc.

    //TODO: Add badges

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof Player otherMember)) {
            return false;
        }
        return otherMember.getId() == getId();
    }

}
