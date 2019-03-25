package GeoGrid2.engine;

import org.joml.Vector3f;
import GeoGrid2.engine.graph.Mesh;


/**
 * This class will be mre useful when implementing many in app meshes, including GUIs and information panels,
 * however at the moment it facilitates transformation for the map (zoom, pan, scale)
 *
 */


public class GridItem {

    private final Mesh mesh;
    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;

    public GridItem(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        scale = 1;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    // No need to rotate currently, however a 45 degree at a time rotation option will be appealing
    // later
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
    
    public Mesh getMesh() {
        return mesh;
    }
}