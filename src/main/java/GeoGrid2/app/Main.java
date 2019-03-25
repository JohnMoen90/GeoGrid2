package GeoGrid2.app;

import GeoGrid2.engine.GridEngine;
import GeoGrid2.engine.IGridLogic;

public class Main {
 
    public static void main(String[] args) {
        try {

            DataTransfer dt = new DataTransfer();   // Not used
            boolean vSync = true;
            IGridLogic gridLogic = new GridController(dt);  // Create new gridController & add to engine
            GridEngine gridEng = new GridEngine("GAME", 600, 480, vSync, gridLogic);
            gridEng.start();    // Start OpenGL
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }


}