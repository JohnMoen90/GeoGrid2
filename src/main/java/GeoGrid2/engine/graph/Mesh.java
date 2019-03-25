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

        // Initialize the buffers - no need to save these
        FloatBuffer posBuffer = null;
        FloatBuffer colourBuffer = null;    // TODO Finish adding textures you stopped here..... gl
        IntBuffer indicesBuffer = null;
        IntBuffer hgtBuffer = null;
        FloatBuffer hgtColorBuffer = null;

        try {

            vertexCount = indices.length; // Set Draw count

            this.tileSize = tileSize;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            lon = 38;
            lat = -111;

            // Initialize Hgt data & get the max and min height
            int[] hgtData = HgtManager.createHgtIntArray(lon, lat);
            maxHgt = Collections.max(Arrays.stream(hgtData).boxed().collect(Collectors.toList()));
            minHgt = Collections.min(Arrays.stream(hgtData).boxed().collect(Collectors.toList()));

            // To test max min heights
//            System.out.println("Max: " + maxHgt);
//            System.out.println("Min: " + minHgt);

            // Calculate color values
            int counter = 0;
            int index = 0;
            float[] hgtColorValues = new float[1201*1201];

            // Set to single decimal for staggered look ------\/
            DecimalFormat df = new DecimalFormat("####.##");
            df.setRoundingMode(RoundingMode.HALF_DOWN);
            for (int i = 0; i < 1201; i++) {
//                int precalcY = 100 * i;            <---To test color values ---\/
                for (int j = 0; j < 1201; j++) {
//                    System.out.printf(j==49? " %d.%.2f \n" : " %d.%.2f ",j+precalcY,levelRange(hgtData[index++], minHgt, maxHgt));
                    float tempFloat = Float.parseFloat(df.format(levelRange(hgtData[counter++],minHgt,maxHgt)));
                    hgtColorValues[index++] = tempFloat;

                }

            }

            // Set the VBOs for the VAO

            // Position VBO
            posVboId = glGenBuffers();  // Get a buffer ID
            posBuffer = MemoryUtil.memAllocFloat(positions.length); // Allocate enough memory
            posBuffer.put(positions).flip();    // Put data in buffer and flip it
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);    // Bind the VBO buffer for writing
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);   // Add actual data to buffer
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0); // Set VBO attributes

            // Colour VBO   <-- Currently does nothing - See fix below
            colourVboId = glGenBuffers();
            colourBuffer = MemoryUtil.memAllocFloat(colours.length);
            colourBuffer.put(colours).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            // Hgt VBO  <-- Currently does nothing - see fix below
            hgtVboId = glGenBuffers();
            hgtBuffer = MemoryUtil.memAllocInt(hgtData.length);
            hgtBuffer.put(hgtData).flip();
            glBindBuffer(GL_ARRAY_BUFFER, hgtVboId);
            glBufferData(GL_ARRAY_BUFFER, hgtBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 1, GL_INT, false, 0, 0);

            //Hgt Color Value VBO       <--- This is a temporary fix, I would like to calculate this data on the GPU
            hgtColorVboId = glGenBuffers();
            hgtColorBuffer = MemoryUtil.memAllocFloat(hgtColorValues.length);
            hgtColorBuffer.put(hgtColorValues).flip();
            glBindBuffer(GL_ARRAY_BUFFER, hgtColorVboId);
            glBufferData(GL_ARRAY_BUFFER, hgtColorBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(3, 1, GL_FLOAT, false, 0, 0);

            // Index VBO    <-- What the draw call is made on normally, currently on vertices due to geometry shader
            idxVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // Release buffer and vao bindings
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {

            // Make sure to free all the memory
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (colourBuffer != null) {
                MemoryUtil.memFree(colourBuffer);
            }
            if (hgtBuffer != null) {
                MemoryUtil.memFree(hgtBuffer);
            }
            if (hgtColorBuffer != null) {
                MemoryUtil.memFree(hgtColorBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    // Find normalized color value from min and max height
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

        // The normal draw call is drawElements, but elements and vertices are 1 to 1 now thanks to geometry shader
        // glDrawElements(GL_POINTS, getVertexCount(), GL_UNSIGNED_INT, 0);
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
        glDeleteBuffers(colourVboId);
        glDeleteBuffers(hgtVboId);
        glDeleteBuffers(hgtColorVboId);
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
