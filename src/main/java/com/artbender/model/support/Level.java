package com.artbender.model.support;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlEnum()
public enum Level {
    A, B, C, D, E, F
}
