package GeoGrid2.game;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectionMap {

    private DataTransfer dt;

    public SelectionMap(DataTransfer dt) {
        this.dt = dt;
        init();

    }

    public void init() {


        JXMapViewer mapViewer = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the focus
        GeoPosition us = new GeoPosition(40, -98);

        mapViewer.setZoom(10);
        mapViewer.setAddressLocation(us);

        // Display the viewer in a JFrame
        JFrame frame = new JFrame("JXMapviewer2 Example 1");
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        mapViewer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
                    java.awt.Point p = e.getPoint();
                    GeoPosition geo = mapViewer.convertPointToGeoPosition(p);
                    dt.setLat((geo.getLatitude()));
                    dt.setLon(geo.getLongitude());
                    frame.setVisible(false);
                    frame.dispose();
                }
            }
        });
    }
}
