package GeoGrid2.engine;

/**
 * I got this code from a lwjgl book - https://github.com/lwjglgamedev/lwjglbook - where I also got the
 * basic design of the boilerplate code in GeoGrid2
 *
 * This class helps easily get elapsed time for steady fps when rendering
 */

public class Timer {



    private double lastLoopTime;
    
    public void init() {
        lastLoopTime = getTime();
    }

    public double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    public double getLastLoopTime() {
        return lastLoopTime;
    }
}