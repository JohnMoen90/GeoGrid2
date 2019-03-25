package GeoGrid2.engine;

/**
 * This class manages the app thread, fps, and binding window with app app
 */

public class GridEngine implements Runnable {

    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    private final Window window;
    private final Thread geoGridLoopThread;
    private final Timer timer;

    private final IGridLogic gameLogic;

    public GridEngine(String windowTitle, int width, int height, boolean vSync, IGridLogic gameLogic) throws Exception {
        geoGridLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    // Starts the thread, runs on single thread overall for mac compatibility
    public void start() {
        String osName = System.getProperty("os.name");
        if ( osName.contains("Mac") ) {
            geoGridLoopThread.run();
        } else {
            geoGridLoopThread.start();
        }
    }

    @Override
    // The main loop starts here
    public void run() {
        try {
            init();
            mainLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        gameLogic.init(window);
    }

    // The main loop
    protected void mainLoop() {

        // All this is to ensure the correct target FPS and update/render sync
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            // This is what stops the loop until render time
            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    protected void cleanup() {
        gameLogic.cleanup();
    }

    // This helps with screen tearing
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }

    protected void input() {
        gameLogic.input(window);
    }

    protected void update(float interval) {
        gameLogic.update(interval);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}
