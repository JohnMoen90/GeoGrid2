package GeometryShader.engine;

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

public class HgtManager {

    public static String filename;
    public static String destDir = "./downloads/";
    public static String url;

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

    static private void buildURL(int lg, int lt) {
        filename = lg > 0 ? "N" : "S";
        filename += Math.abs(lg);

        filename += lt > 0 ? "E" : "W";
        filename += Math.abs(lt) > 99 ? Math.abs(lt) : "0"+Math.abs(lt);

        url = "https://dds.cr.usgs.gov/srtm/version2_1/SRTM3/North_America/" + filename + ".hgt.zip";

    }

    static public void unzip() {

        try {
            ZipFile zipFile = new ZipFile(destDir + filename + ".hgt.zip");
            zipFile.extractAll(destDir);
        } catch (ZipException ze) {
            System.out.println("Zip Error!?");
            ze.printStackTrace();
        }

    }


    static public int[] createHgtIntArray(int lg, int lt) throws Exception {

        buildURL(lg,lt);
        File f = new File(destDir + filename + ".hgt");
        if (!f.exists()) {
            retrieveData();
            unzip();
        }

        int[] hgtArray = new int[1201*1201];
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        File file = new File(classLoader.getResource("N45W115.hgt").getFile());
        try (FileChannel fc = new FileInputStream(f).getChannel()) {

            ByteBuffer bb = ByteBuffer.allocateDirect((int) fc.size());

            while (bb.remaining() > 0) fc.read(bb);
            bb.flip();

            ShortBuffer sb = bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer();

            int index = 0;

            for (int i = 0; i < 1201; i++) {
                int precalcY = 1201 * i;
                for (int j = 0; j < 1201; j++) {
//                    System.out.printf(j==49? " %d.%d \n" : " %d.%d ",j,sb.get(j + precalcY));
                    if (sb.get(j + precalcY) == -32768) {
                        hgtArray[index++] = -10;
                    } else {
                        hgtArray[index++] = (int) sb.get(j+precalcY) < 0 ? 0 : sb.get(j+precalcY);
                    }
                }

            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return hgtArray;
    }

}
