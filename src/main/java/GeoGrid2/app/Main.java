package GeoGrid2.app;

import GeoGrid2.engine.GridEngine;
import GeoGrid2.engine.IGridLogic;

public class Main {
 
    public static void main(String[] args) {
        try {

            System.out.println("Welcome to GeoGrid2!\n");
            System.out.println("A random part of the Rockies is being chosen for you...");
            System.out.println("An altitude rounding scheme is being chosen for you...");
            DataTransfer dt = new DataTransfer();   // Not used
            boolean vSync = true;
            IGridLogic gridLogic = new GridController(dt);  // Create new gridController & add to engine
            GridEngine gridEng = new GridEngine("GeoGrid2", 600, 480, vSync, gridLogic);
            gridEng.start();    // Start OpenGL
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }


}