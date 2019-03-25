package GeoGrid2.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * This class manages the GLFW window for use with openGL.
 */

public class Window {

    private final String title;
    private long windowID;

    private int width;
    private int height;
    private boolean vSync;

    private boolean resized;

    private MouseHandler mouseCallback;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
    }

    public void init() {

        // Set error callback.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. !!GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // A bunch of 'window hints', basically window settings.
        glfwDefaultWindowHints(); //Set defaults
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // Invisible until init finishes
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);

        // Hints for openGL
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // Create window
        windowID = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowID == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowID, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        // Set up cursor position callback
        glfwSetCursorPosCallback(windowID, mouseCallback = new MouseHandler());

        // Setup key callback.
        glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) { // Upon release of button
                glfwSetWindowShouldClose(window, true); // Actually closed in GridEngine
            }
        });

        // Get resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Center window
        glfwSetWindowPos(
                windowID,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowID);

        if (isvSync()) {
            // Enable v-sync
            glfwSwapInterval(1);
        }

        // Make the window visible
        glfwShowWindow(windowID);

        // Allows OpenGL commands to run
        GL.createCapabilities();

        // Set the clear color - currently sets to default
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }
    
    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowID, keyCode) == GLFW_PRESS;
    }
    
    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowID);
    }
    
    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public void update() {
        glfwSwapBuffers(windowID);
        glfwPollEvents();
    }

    public MouseHandler getMouseHandler() {
        return mouseCallback;
    }
}