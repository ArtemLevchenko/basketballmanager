package com.artbender.model.attribute;

import com.artbender.model.support.Level;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "skills")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Skills implements Serializable {
    // All offensive + defensive actions
    @XmlElement
    private Level iq;
    // offensive inside
    @XmlElement
    private Level athleticism;
    // offensive medium
    @XmlElement
    private Level shotIq;
    // offensive 3pt
    @XmlElement
    private Level outsideScoring;
    // defensive interior
    @XmlElement
    private Level interiorDefense;
    // defensive mid
    @XmlElement
    private Level midDefense;
    // defensive perimeter
    @XmlElement
    private Level perimeterDefense;
}
