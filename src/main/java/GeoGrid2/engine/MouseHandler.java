package GeoGrid2.engine;
import org.lwjgl.glfw.GLFWCursorPosCallback;


/**
 * This is for interacting with the mouse
 * Not currently used.
 */
public class MouseHandler extends GLFWCursorPosCallback {

    private double xPos;
    private double yPos;

    @Override
    public void invoke(long window, double xpos, double ypos) {
        xPos = xpos;
        yPos = ypos;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

}
