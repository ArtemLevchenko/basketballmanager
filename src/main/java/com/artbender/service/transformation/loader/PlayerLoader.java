package com.artbender.service.transformation.loader;

import com.artbender.model.db.Player;
import com.artbender.service.transformation.converter.PlayerConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class PlayerLoader implements LoaderStrategy<Player> {
    private final String LOAD_PATH_START = "/game/roster/";
    private final String LOAD_PATH_END = "_roster.xml";

    private PlayerConverter converter;

    @Autowired
    public PlayerLoader(PlayerConverter converter) {
        this.converter = converter;
    }

    @Override
    public List<Player> loadData(String pathName) {
        converter.setFileOut(PlayerConverter.class.getResourceAsStream(LOAD_PATH_START + pathName + LOAD_PATH_END));
        return converter.loadData();
    }

}
