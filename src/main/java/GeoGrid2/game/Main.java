package GeoGrid2.game;

import GeoGrid2.engine.GameEngine;
import GeoGrid2.engine.IGameLogic;

public class Main {
 
    public static void main(String[] args) {
        try {
            DataTransfer dt = new DataTransfer();
//            new SettingsDialog(dt);

            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame(dt);
            GameEngine gameEng = new GameEngine("GAME", 600, 480, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }


}