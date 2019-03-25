package GeoGrid2.app;

import GeoGrid2.engine.MapTile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is not finished!!
 * The start of sqlite CRUD functionality
 */

public class DatabaseIO {

    static String db_url = "jdbc:sqlite:us_hgt_data.db";


    public boolean addMapTile(MapTile mp) {

        final String addMapTileSql = "INSERT INTO map_tiles VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(db_url);
             PreparedStatement addMapTilePS = conn.prepareStatement(addMapTileSql)) {



        } catch (SQLException sqle) {

            if (sqle.getMessage().contains("unique constraint")) {
                System.out.println("This product is already in the Database!");
                return false;
            }

            System.out.println("Something happened...");
            sqle.printStackTrace();
            return false;
        }


        return true;
    }

}
