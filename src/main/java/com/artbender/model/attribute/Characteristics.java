package com.artbender.model.attribute;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "characteristics")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Characteristics implements Serializable {

    @XmlElement
    private int totalRank;
    @XmlElement
    private int insideScore;
    @XmlElement
    private int mediumShot;
    @XmlElement
    private int threeShot;
    @XmlElement
    private int freeThrow;
    @XmlElement
    private int dunk;
    @XmlElement
    private int pass;
    @XmlElement
    private int ballSecurity;
    @XmlElement
    private int block;
    @XmlElement
    private int steal;
    @XmlElement
    private int ofRebound;
    @XmlElement
    private int defRebound;
    @XmlElement
    private int stamina;
    @XmlElement
    private int defenseOnBall;
    @XmlElement
    private int currentStamina;
}
