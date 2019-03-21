package GeoGrid2.engine;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseHandler extends GLFWCursorPosCallback {

    private double xPos;

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    private double yPos;

    @Override
    public void invoke(long window, double xpos, double ypos) {

        xPos = xpos;
        yPos = ypos;
    }

}
