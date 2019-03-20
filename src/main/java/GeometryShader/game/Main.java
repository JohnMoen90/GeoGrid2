package GeometryShader.game;

import GeometryShader.engine.GameEngine;
import GeometryShader.engine.IGameLogic;

import javax.xml.crypto.Data;

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