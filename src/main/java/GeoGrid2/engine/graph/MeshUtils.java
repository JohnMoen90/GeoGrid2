package GeoGrid2.engine.graph;

public class MeshUtils {

    private int numTilesX;
    private int numTilesY;


    public MeshUtils(int numTilesX, int numTilesY) {
        this.numTilesX = numTilesX;
        this.numTilesY = numTilesY;
    }


    public float[] createColorArray(){
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


    public int[] createIndiceArray() {
        int [] indiceArray = new int[numTilesX * numTilesY];

        for (int i = 0; i < indiceArray.length; i++) {
            indiceArray[i] = i;
        }
//        for (int vert : indiceArray) {
//            System.out.println(vert);
//        }
        return indiceArray;
    }

    public float[] createVertexArray(){

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
}
