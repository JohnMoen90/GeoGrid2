package GeoGrid2.engine.graph;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL12.glTexImage3D;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private int id;

    public Texture(String filename) throws Exception {
        this(loadTexture(filename));
    }

    public Texture(int id) {this.id = id;}

    public void bind() {glBindTexture(GL_TEXTURE_2D, id);}

    public int getId() {
        return id;
    }


    private static int loadTexture(String fileName) throws Exception {
        BufferedImage bi;
        int width;
        int height;
        int id;
        try {
            // Import image and read to a buffered image
            bi = ImageIO.read(new File("./textures/"+fileName));
            width = bi.getWidth();
            height = bi.getHeight();

            // Get the RGB values of every pixel in the image
            int[] pixels_raw;
            pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

            // This is where the pixels are stored/saved to later
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

            // Put pixel data from buffered image into byte buffer
            for(int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pixel = pixels_raw[i * width + j];
                    pixels.put((byte)(pixel >> 16 & 0xFF)); //RED       This is being set to a RGBA color model
                    pixels.put((byte)(pixel >> 8 & 0xFF));  //GREEN
                    pixels.put((byte)(pixel & 0xFF));       //BLUE
                    pixels.put((byte)(pixel >> 24 & 0xFF)); //ALPHA
                }
            }

            // You have to flip from read to write for some reason
            pixels.flip();

            int textureId = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, textureId);

            // Tells openGL giw ti unpack rgba bytes. Each component is 1 byte
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexImage3D(GL_TEXTURE_2D_ARRAY,
                    0,
                    GL_RGBA,
                    width,
                    height,
                    64,
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    pixels);
            glGenerateMipmap(GL_TEXTURE_2D_ARRAY);

            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAX_LEVEL, GL_MODULATE);
//            glTexParameteri(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
//            glTexParameteri(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

//            stbi_image_free(buf);

            return textureId;
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return 0;
    }
}



