package com.artbender.model.tendency;

import com.artbender.model.exceptions.NbaManagerException;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "substitutionRating")
@XmlEnum()
public enum SubstitutionRating {

    ZERO(0), TEN(10), TWENTY(20), THIRTY(30), FOURTY(40), FIFTY(50);

    private final Integer label;

    SubstitutionRating(Integer label) {
        this.label = label;
    }

    public Integer getLabel() {
        return this.label;
    }

    public static SubstitutionRating convertValueToName(Integer value) {
        switch(value) {
            case 0:
                return SubstitutionRating.ZERO;
            case 10:
                return SubstitutionRating.TEN;
            case 20:
                return SubstitutionRating.TWENTY;
            case 30:
                return SubstitutionRating.THIRTY;
            case 40:
                return SubstitutionRating.FOURTY;
            case 50:
                return SubstitutionRating.FIFTY;
        }
        throw new NbaManagerException("Incorrect value of substitution rating!");
    }
}
