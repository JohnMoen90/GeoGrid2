package GeoGrid2.engine.graph;

/**
 * This class is a toolbox for creating arrays intended to be put into VBOs
 */
public class MeshUtils {

    private int numTilesX;
    private int numTilesY;


    // A meshUtils object takes number of tiles as a parameter and does all the calculations
    // with the instance variables
    public MeshUtils(int numTilesX, int numTilesY) {
        this.numTilesX = numTilesX;
        this.numTilesY = numTilesY;
    }

    // Creates random colors for every tile
    public float[] createColorArray(){
        int index = 0;
        float[] colorArray = new float[numTilesY * numTilesX * 3];  //<- 3 for r,g,b

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

    // Creates a big list of 0-n numbers
    public int[] createIndiceArray() {
        int [] indiceArray = new int[numTilesX * numTilesY];
        for (int i = 0; i < indiceArray.length; i++) {
            indiceArray[i] = i;
        }
        return indiceArray;
    }

    // Creates an array of normalized vertex positions at center of each tile
    public float[] createVertexArray(){

        float[] gridArray = new float[numTilesX * numTilesY * 2]; //<-2 for x,y

        float tileW = (float) 2 / numTilesX;    // Values are -1 to 1
        float tileH = (float) 2 / numTilesY;

        int index = 0;

        float YCursor = 1;  // Set YCursor to 1 so top down
        YCursor -= tileH /2;    // Start with a half tile height down
        for (int y = 0; y < numTilesY; y++) {
            float xCursor = -1; //Set XCursor to -1 so right left
            xCursor += tileW / 2;   // Start with a half tile width right
            for (int x = 0; x < numTilesX * 2; x = x + 2) {
                gridArray[index++] = xCursor;   // Add the values
                gridArray[index++] = YCursor;
                xCursor += tileW; // remove one tile width each time
            }
            YCursor -= tileH;// remove one tile height each time
        }
        return gridArray;
    }
}
