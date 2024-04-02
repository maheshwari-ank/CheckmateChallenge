package com.grandmasters.checkmatechallenge;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    public List<ChessLevel> gameConfigs = new ArrayList<>();



    public List<ChessLevel> getGameConfigs() {
        return gameConfigs;
    }

    public void setGameConfigs(List<ChessLevel> gameConfigs) {
        this.gameConfigs = gameConfigs;
    }

}
