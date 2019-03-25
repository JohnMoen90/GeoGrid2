package GeoGrid2.app;

import GeoGrid2.engine.GridItem;
import GeoGrid2.engine.graph.MeshUtils;
import org.joml.Vector3f;
import GeoGrid2.engine.IGridLogic;
import GeoGrid2.engine.Window;
import GeoGrid2.engine.graph.Mesh;

import static org.lwjgl.glfw.GLFW.*;

public class GridController implements IGridLogic {

    private final DataTransfer dt;

    private int displxInc = 0;
    private int displyInc = 0;
    private int displzInc = 0;

    private int scaleInc = 0;

    private MeshUtils meshUtils;

    private int numTilesX;
    private int numTilesY;

    float tileSize;

    private final Renderer renderer;

    // A list of all items to be rendered, currently there is only one
    private GridItem[] gridItems;

    public GridController(DataTransfer dt) {
        renderer = new Renderer();
        this.dt = dt;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        numTilesX = 1201;   // Hardcoded for 3' arc second data
        numTilesY = 1201;

        meshUtils = new MeshUtils(numTilesX, numTilesY);

        // Normalize tile size
        tileSize = (float) 2 / numTilesX;

        // Create arrays for the VAO's VBOs
        float[] positions = meshUtils.createVertexArray();
        float[] colours = meshUtils.createColorArray(); // Not used!!
        int[] indices = meshUtils.createIndiceArray();  // Not used!!

        // Initialized everything and load new GridItem into gridItem list
        Mesh mesh = new Mesh(positions, colours, indices, tileSize);
        GridItem gridItem = new GridItem(mesh);
        gridItem.setPosition(0, 0, -2);
        gridItems = new GridItem[] {gridItem};
    }


    @Override
    public void input(Window window) {
        displyInc = 0;
        displxInc = 0;
        displzInc = 0;
        scaleInc = 0;
        if (window.isKeyPressed(GLFW_KEY_UP)) { // Move map up
            displyInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {    // Move map down
            displyInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {    // Move map left
            displxInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {   // Move map right
            displxInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {   // Zoom out
            displzInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {   // Zoom in
            displzInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {   // Decrease scale
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {   // Increase scale
            scaleInc = 1;
        }
    }

    @Override
    public void update(float interval) {

        for (GridItem gridItem : gridItems) {
            float scale = gridItem.getScale();

            // Update position
            Vector3f itemPos = gridItem.getPosition();
            float posx = itemPos.x + displxInc * (scale * 0.01f);
            float posy = itemPos.y + displyInc * (scale * 0.01f);
            float posz = itemPos.z + displzInc * (scale * 0.01f);
            gridItem.setPosition(posx, posy, posz);
            
            // Update scale
            scale += scaleInc * (scale * 0.05f);
            if ( scale < 0 ) {
                scale = 0;
            }
            gridItem.setScale(scale);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, gridItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GridItem gridItem : gridItems) {
            gridItem.getMesh().cleanUp();
        }
    }

    public int getNumTilesX() {
        return numTilesX;
    }

    public int getNumTilesY() {
        return numTilesY;
    }

}
