package com.artbender.model.db;

import com.artbender.model.plan.CoachGamePlan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Coach implements Serializable {
    @XmlElement
    private int id;
    @XmlElement
    private String name;
    private Team team;
    private CoachGamePlan coachGamePlan;
}
