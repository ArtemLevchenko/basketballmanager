package com.artbender.service.transformation.converter;

import com.artbender.model.db.Coach;
import com.artbender.model.dto.load.CoachDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@NoArgsConstructor
@Component
public class CoachConverter extends AbstractBaseConverter implements ConverterStrategy<Coach>{

    @Override
    public List<Coach> loadData() {
        List<Coach> items = null;
        try {
            jaxbContext = JAXBContext.newInstance(CoachDTO.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            CoachDTO itemDTO = (CoachDTO) jaxbUnmarshaller.unmarshal(fileOut);
            items = itemDTO.getCoaches();
        } catch (JAXBException ex) {
            Logger.getLogger(CoachConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }
}
