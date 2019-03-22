package GeoGrid2.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * This facilitates the zooming, moving, and scaling capabilities
 */

public class Transformation {

    private final Matrix4f projectionMatrix;

    private final Matrix4f worldMatrix;
    
    public Transformation() {
        worldMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
    }

    // Here this mostly allows the box shaped world to not be tied to view space strictly
    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;        
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    // Currently rotation is disabled
    public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
        worldMatrix.identity().translate(offset).
                rotateX((float)Math.toRadians(rotation.x)).
                rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).
                scale(scale);
        return worldMatrix;
    }
}
