package GeoGrid2.engine.graph;

import GeoGrid2.engine.HgtManager;
import org.lwjgl.system.MemoryUtil;

import java.math.RoundingMode;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final int vaoId;

    private final int posVboId;
    private final int colourVboId;
    private final int idxVboId;
    private int hgtVboId;
    private int hgtColorVboId;
    private int textureId;

    private final int vertexCount;

    private float tileSize;

    private int maxHgt;
    private int minHgt;

    private int lon;
    private int lat;

    public Mesh(float[] positions, float[] colours, int[] indices, float tileSize) throws Exception{
        FloatBuffer posBuffer = null;
        FloatBuffer colourBuffer = null;    // TODO Finish adding textures you stopped here..... gl
        IntBuffer indicesBuffer = null;
        IntBuffer hgtBuffer = null;
        FloatBuffer hgtColorBuffer = null;

        try {
            vertexCount = indices.length;
            this.tileSize = tileSize;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            lon = 46;
            lat = -113;

            // Create Hgt data
            int[] hgtData = HgtManager.createHgtIntArray(lon, lat);

            maxHgt = Collections.max(Arrays.stream(hgtData).boxed().collect(Collectors.toList()));
            minHgt = Collections.min(Arrays.stream(hgtData).boxed().collect(Collectors.toList()));

            System.out.println("Max: " + maxHgt);
            System.out.println("Min: " + minHgt);
            int counter = 0;
            int index = 0;
            float[] hgtColorValues = new float[1201*1201];

            DecimalFormat df = new DecimalFormat("####.#");
            df.setRoundingMode(RoundingMode.HALF_DOWN);

            for (int i = 0; i < 1201; i++) {
                int precalcY = 100 * i;
                for (int j = 0; j < 1201; j++) {
//                    System.out.printf(j==49? " %d.%.2f \n" : " %d.%.2f ",j+precalcY,levelRange(hgtData[index++], minHgt, maxHgt));
                    float tempFloat = Float.parseFloat(df.format(levelRange(hgtData[counter++],minHgt,maxHgt)));

                    hgtColorValues[index++] = tempFloat;

                }

            }




            // Position VBO
            posVboId = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

            // Colour VBO
            colourVboId = glGenBuffers();
            colourBuffer = MemoryUtil.memAllocFloat(colours.length);
            colourBuffer.put(colours).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);


            // Hgt VBO
            hgtVboId = glGenBuffers();
            hgtBuffer = MemoryUtil.memAllocInt(hgtData.length);
            hgtBuffer.put(hgtData).flip();
            glBindBuffer(GL_ARRAY_BUFFER, hgtVboId);
            glBufferData(GL_ARRAY_BUFFER, hgtBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 1, GL_INT, false, 0, 0);

            //Hgt Color Value VBO
            hgtColorVboId = glGenBuffers();
            hgtColorBuffer = MemoryUtil.memAllocFloat(hgtColorValues.length);
            hgtColorBuffer.put(hgtColorValues).flip();
            glBindBuffer(GL_ARRAY_BUFFER, hgtColorVboId);
            glBufferData(GL_ARRAY_BUFFER, hgtColorBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(3, 1, GL_FLOAT, false, 0, 0);



            // Index VBO
            idxVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (colourBuffer != null) {
                MemoryUtil.memFree(colourBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    private float levelRange(int hgt, int minHgt, float maxHgt){
        int tempMin = minHgt + 1000;
        return ((hgt - tempMin) / (maxHgt - tempMin));
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void render() {
        // Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

//        glDrawElements(GL_POINTS, getVertexCount(), GL_UNSIGNED_INT, 0);
        glDrawArrays(GL_POINTS, 0, vertexCount);
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(idxVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public float getTileSize() {
        return tileSize;
    }

    public int getMaxHgt() {
        return maxHgt;
    }
}
