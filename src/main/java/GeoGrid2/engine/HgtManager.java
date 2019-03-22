package GeoGrid2.engine;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * This class is kind of a huge mess.. but it works!
 * It pulls the data we need from offline, opens the zip it comes in,
 * and converts that data into an array
 */
public class HgtManager {

    public static String filename;
    public static String destDir = "./downloads/";
    public static String url;


    // Creates an array of the Hgt data in the file
    // This is the only method that is called in Mesh
    static public int[] createHgtIntArray(int lg, int lt) throws Exception {
        buildURL(lg,lt);
        File f = new File(destDir + filename + ".hgt");
        if (!f.exists()) {
            retrieveData();
            unzip();
        }

        int[] hgtArray = new int[3601 * 3601];    // 3arc second data are always these dimensions
                                                // 1arc second would be 3 times the size

        // For trying to load from resources, had some strange issues so made new folder
        //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //File file = new File(classLoader.getResource("N45W115.hgt").getFile());

        // Open hgt file
        try (FileChannel fc = new FileInputStream(f).getChannel()) {

            ByteBuffer bb = ByteBuffer.allocateDirect((int) fc.size()); // Create buffer

            while (bb.remaining() > 0) fc.read(bb); // Read to buffer
            bb.flip();  // Flip out

            // This data happens to be BIG_ENDIAN
            ShortBuffer sb = bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer();

            // Write the data from buffer to array
            int index = 0;
            for (int i = 0; i < 3601; i++) {
                int precalcY = 3601 * i;    // Useful when wanting less than the entire array
                for (int j = 0; j < 3601; j++) {

                    // Print test
                    // System.out.printf(j==49? " %d.%d \n" : " %d.%d ",j,sb.get(j + precalcY));
                    if (sb.get(index) == -32768) {   // Means Invalid Data
                        hgtArray[index++] = -10;    // Change it to -10 for my own easy reference later
                    } else {    // If data is less than 0 just make it 0
                        try {
                            hgtArray[index++] = (int) sb.get(index) < 0 ? 0 : sb.get(index);
                        } catch (Exception e) {
                            System.out.println(index);
                            e.printStackTrace();
                        }
                    }
                }

            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return hgtArray;
    }


    // Grabs the file from offline and copies it to download folder
    static public void retrieveData() {
        try(InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(destDir + filename + ".hgt.zip"), StandardCopyOption.REPLACE_EXISTING);
        } catch (MalformedURLException urle) {
            System.out.println("Problem with Source URL");
            urle.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Something happened...");
            ioe.printStackTrace();
        }
    }

    // Creates a url based on long / lat value
    static private void buildURL(int lg, int lt) {

        filename = lg > 0 ? "N" : "S";
        filename += Math.abs(lg);

        filename += lt > 0 ? "E" : "W";
        filename += Math.abs(lt) > 99 ? Math.abs(lt) : "0"+Math.abs(lt);

        url = "https://dds.cr.usgs.gov/srtm/version1/United_States_1arcsec/1arcsec/" + filename + ".hgt.zip";

    }

    // Unzips the hgt.zip into the same folder
    static public void unzip() {
        try {
            ZipFile zipFile = new ZipFile(destDir + filename + ".hgt.zip");
            zipFile.extractAll(destDir);
        } catch (ZipException ze) {
            System.out.println("Zip Error!?");
            ze.printStackTrace();
        }

    }

}
