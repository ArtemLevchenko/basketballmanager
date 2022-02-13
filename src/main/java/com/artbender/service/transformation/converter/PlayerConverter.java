package com.artbender.service.transformation.converter;

import com.artbender.model.db.Player;
import com.artbender.model.dto.load.PlayersDTO;
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
public class PlayerConverter extends AbstractBaseConverter implements ConverterStrategy<Player> {

    @Override
    public List<Player> loadData() {
        List<Player> items = null;
        try {
            jaxbContext = JAXBContext.newInstance(PlayersDTO.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PlayersDTO itemDTO = (PlayersDTO) jaxbUnmarshaller.unmarshal(fileOut);
            items = itemDTO.getPlayers();
        } catch (JAXBException ex) {
            Logger.getLogger(PlayerConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }
}
