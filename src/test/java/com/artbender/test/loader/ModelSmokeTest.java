package com.artbender.test.loader;

import com.artbender.model.dto.rest.GlobalGameRequest;
import com.artbender.service.ServiceConfig;
import com.artbender.service.orchestrator.GameOrchestrator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class ModelSmokeTest {

    @Autowired
    private GameOrchestrator gameOrchestrator;

    @Test
    public void test() throws Exception {
        GlobalGameRequest teamGameRequest = new GlobalGameRequest();
        teamGameRequest.setHomeTeam("cleveland");
        teamGameRequest.setAwayTeam("miami");
        gameOrchestrator.initGame(teamGameRequest);
        while (!gameOrchestrator.isEndGame()) {
            gameOrchestrator.execute();
        }
        System.out.println("|");
        Assert.assertEquals("END GAME", gameOrchestrator.isEndGame(), Boolean.TRUE);
    }


}
