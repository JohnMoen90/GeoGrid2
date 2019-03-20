package GeometryShader.game;

import org.joml.Vector3f;
import GeometryShader.engine.GameItem;
import GeometryShader.engine.IGameLogic;
import GeometryShader.engine.Window;
import GeometryShader.engine.graph.Mesh;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;

    int numTilesX;
    int numTilesY;

    float tileSize;

    private final Renderer renderer;

    private GameItem[] gameItems;

    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        numTilesX = 1201;
        numTilesY = 1201;

        tileSize = (float) 2 / numTilesX;

        // Create the Mesh
//        float[] positions = new float[]{
//                -0.5f,  0.5f, // top-left
//                0.5f,  0.5f, // top-right
//                0.5f, -0.5f, // bottom-right
//                -0.5f, -0.5f  // bottom-left
//        };

        float[] positions = createVertexArray();
        float[] colours = createColorArray();


        int[] indices = createIndiceArray();
        Mesh mesh = new Mesh(positions, colours, indices, tileSize);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setPosition(0, 0, -2);
        gameItems = new GameItem[] { gameItem };
    }

    private float[] createColorArray(){
    int index = 0;
        float[] colorArray = new float[numTilesY * numTilesX * 3];
        float r = (float) Math.random();
        float b = (float) Math.random();
        float g = (float) Math.random();
        for (int i = 0; i < colorArray.length; i = i + 3) {
            colorArray[index++] = r;
            colorArray[index++] = b;
            colorArray[index++] = g;
        }
        return colorArray;
    }


    private int[] createIndiceArray() {
        int [] indiceArray = new int[numTilesX * numTilesY];

        for (int i = 0; i < indiceArray.length; i++) {
            indiceArray[i] = i;
        }
//        for (int vert : indiceArray) {
//            System.out.println(vert);
//        }
        return indiceArray;
    }

    private float[] createVertexArray(){

        float[] gridArray = new float[numTilesX * numTilesY * 2];

        float tileW = (float) 2 / numTilesX;
        float tileH = (float) 2 / numTilesY;

        int index = 0;

        float YCursor = 1;
        YCursor -= tileH /2;
        for (int y = 0; y < numTilesY; y++) {
            float xCursor = -1;
            xCursor += tileW / 2;
            for (int x = 0; x < numTilesX * 2; x = x + 2) {
                gridArray[index++] = xCursor;
                gridArray[index++] = YCursor;
                xCursor += tileW;
            }
            YCursor -= tileH;

        }
//        for (float vert : gridArray) {
//            System.out.println(vert);
//        }
        return gridArray;
    }


    @Override
    public void input(Window window) {
        displyInc = 0;
        displxInc = 0;
        displzInc = 0;
        scaleInc = 0;
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }

    @Override
    public void update(float interval) {
        for (GameItem gameItem : gameItems) {
            float scale = gameItem.getScale();
            // Update position
            Vector3f itemPos = gameItem.getPosition();
            float posx = itemPos.x + displxInc * (scale * 0.01f);
            float posy = itemPos.y + displyInc * (scale * 0.01f);
            float posz = itemPos.z + displzInc * (scale * 0.01f);
            gameItem.setPosition(posx, posy, posz);
            
            // Update scale
            scale += scaleInc * (scale * 0.05f);
            if ( scale < 0 ) {
                scale = 0;
            }
            gameItem.setScale(scale);
            
            // Update rotation angle
//            float rotation = gameItem.getRotation().z + 1.5f;
//            if ( rotation > 360 ) {
//                rotation = 0;
//            }
//            gameItem.setRotation(0, 0, rotation);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }

}
