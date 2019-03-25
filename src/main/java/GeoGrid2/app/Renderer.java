package GeoGrid2.app;

import GeoGrid2.engine.GridItem;
import org.joml.Matrix4f;
import GeoGrid2.engine.Utils;
import GeoGrid2.engine.Window;
import GeoGrid2.engine.graph.ShaderProgram;
import GeoGrid2.engine.graph.Transformation;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    // FOV in radians and depth limits
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
        
        // Create uniforms for world and projection matrices and tile size
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("tileSize");
//        shaderProgram.createUniform("mousePos");
//        shaderProgram.createUniform("maxHgt");

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    // Clear the screen
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }


    public void render(Window window, GridItem[] gridItems) {
        clear();

        // Change viewport size to match window size each frame
        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        // Bind the shader program
        shaderProgram.bind();


        // Will be used when textures are implemented
//        shaderProgram.setUniform("texture_sampler", 0);
        // Activate texture bank
//        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
//        glBindTexture(GL_TEXTURE_2D, gridItems[0].getMesh().getTextureId());


        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);

        // Set some uniforms
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
//        shaderProgram.setUniform("maxHgt", (float) gridItems[0].getMesh().getMaxHgt());
//        shaderProgram.setUniform("mousePos", window.getMouseHandler().getxPos(), window.getMouseHandler().getyPos());

        // Render each gameItem
        for(GridItem gridItem : gridItems) {
            shaderProgram.setUniform("tileSize", gridItem.getScale() * gridItems[0].getMesh().getTileSize());

            // Set world matrix for item
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    gridItem.getPosition(),
                    gridItem.getRotation(),
                    gridItem.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);

            // Render the mesh for this grid item
            gridItem.getMesh().render();
        }

        // Release the shader program
        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
