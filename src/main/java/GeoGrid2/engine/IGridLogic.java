package GeoGrid2.engine;

/**
 * For interfacing a program with the grid engine
 * It is implemented by GridCOntroller
 */

public interface IGridLogic {

    void init(Window window) throws Exception;
    
    void input(Window window);

    void update(float interval);
    
    void render(Window window);
    
    void cleanup();
}