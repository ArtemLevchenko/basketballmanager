package com.artbender.model.plan;

import com.artbender.model.tendency.GameFocusTendency;
import com.artbender.model.tendency.OffensiveFocusTendency;
import com.artbender.model.tendency.ShootingFocusTendency;
import com.artbender.model.tendency.SupportFocusTendency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "playerGamePlan")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class PlayerGamePlan implements Serializable {

    private ShootingFocusTendency insideOutside;
    private GameFocusTendency offenseDefense;
    private SupportFocusTendency reboundAssist;
    private OffensiveFocusTendency screenOpening;

}
