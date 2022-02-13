package com.artbender.service.transformation.converter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.JAXBContext;
import java.io.InputStream;

@Setter
@Getter
@NoArgsConstructor
public abstract class AbstractBaseConverter {
    protected JAXBContext jaxbContext;
    protected InputStream fileOut;
}
