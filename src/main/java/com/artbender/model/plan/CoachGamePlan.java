package com.artbender.model.plan;

import com.artbender.model.tendency.GameFocusTendency;
import com.artbender.model.tendency.ShootingFocusTendency;
import com.artbender.model.tendency.SubstitutionRating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "coachGamePlan")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class CoachGamePlan implements Serializable {

    @XmlElement
    private ShootingFocusTendency insideOutside;
    @XmlElement
    private GameFocusTendency offenseDefense;
    @XmlElement
    private SubstitutionRating substitutionRating;
}
