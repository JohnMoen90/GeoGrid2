package GeoGrid2.engine.graph;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

/**
 * This Class is an object that represents the shader program  in OpenGL
 * It takes shader files from resources, parses, compiles, and otherwise totally prepares
 * the shaders for use
 */
public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;
    private int fragmentShaderId;
    private int geometryShaderId;

    private final Map<String, Integer> uniforms;

    public ShaderProgram() throws Exception {
        programId = glCreateProgram();  // Returns an ID
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
        uniforms = new HashMap<>();
    }


    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    // The following overloaded methods all prepare uniforms of different datatypes
    public void setUniform(String uniformName, Matrix4f value) {

        // Matrix requires a float buffer before being set to uniform
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    // Normally for MaxHgt
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, float value) {
            glUniform1f(uniforms.get(uniformName), value);

    }

    // Will be used to find hgt at mouse location
    public void setUniform(String uniformName, double xpos, double ypos) {
        glUniform2f(uniforms.get(uniformName), (float) xpos, (float) ypos);
    }


    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    public void createGeometryShader(String shaderCode) throws Exception {
        geometryShaderId = createShader(shaderCode, GL_GEOMETRY_SHADER);
    }


    // After type is determined compile the shaders
    protected int createShader(String shaderCode, int shaderType) throws Exception {

        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        // Set the code String and compile
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        // Make sure it compiled!
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        // Attach the shader for rendering - still need to link and bind program
        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {

        // Link and make sure program links
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        // Detach the shaders now that they are loaded
        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }
        if (geometryShaderId != 0) {
            glDetachShader(programId, geometryShaderId);
        }

        // Validate the program for some reason
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    // De/Activates the program
    public void bind() {
        glUseProgram(programId);
    }
    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
