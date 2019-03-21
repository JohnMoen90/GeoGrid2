package GeoGrid2.game;

import org.joml.Matrix4f;
import GeoGrid2.engine.GameItem;
import GeoGrid2.engine.Utils;
import GeoGrid2.engine.Window;
import GeoGrid2.engine.graph.ShaderProgram;
import GeoGrid2.engine.graph.Transformation;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Transformation transformation;

    private ShaderProgram shaderProgram;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        shaderProgram.createGeometryShader(Utils.loadResource("/shaders/geometry.geo"));
        shaderProgram.link();
        
        // Create uniforms for world and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("tileSize");
//        shaderProgram.createUniform("mousePos");
//        shaderProgram.createUniform("maxHgt");

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, GameItem[] gameItems) {
        clear();

        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

//        shaderProgram.setUniform("texture_sampler", 0);

//        // Activate firs texture bank
//        glActiveTexture(GL_TEXTURE0);
//        // Bind the texture
//        glBindTexture(GL_TEXTURE_2D, gameItems[0].getMesh().getTextureId());
        
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
//        shaderProgram.setUniform("maxHgt", (float) gameItems[0].getMesh().getMaxHgt());
//        shaderProgram.setUniform("mousePos", window.getMouseHandler().getxPos(), window.getMouseHandler().getyPos());

        // Render each gameItem
        for(GameItem gameItem : gameItems) {
            shaderProgram.setUniform("tileSize", gameItem.getScale() * gameItems[0].getMesh().getTileSize());

            // Set world matrix for this item
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    gameItem.getPosition(),
                    gameItem.getRotation(),
                    gameItem.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            // Render the mes for this game item
            gameItem.getMesh().render();
        }

        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
