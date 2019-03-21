import GeoGrid2.engine.graph.MeshUtils;
import org.junit.Assert;
import org.junit.Test;

public class VaoUtilsTest {
    @Test
    public void testVertexArray() throws Exception {

        // Arrange
        final double[] expectedDBLs = new double[] {-.5,.5,.5,.5,-.5,-.5,.5,-.5};
        float[] expected =  new float[expectedDBLs.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = (float)expectedDBLs[i];
        }

        // Act
        final float[] actual = new MeshUtils(2,2).createVertexArray();

        // Assert
        Assert.assertArrayEquals(actual, expected, (float).1);
    }

    @Test
    public void testIndicesArray() throws Exception {

        // Arrange
        final int[] expected = new int[]{0,1,2,3,4,5,6,7,8};

        // Act
        final int[] actual = new MeshUtils(3,3).createIndiceArray();

        // Assert
        Assert.assertArrayEquals(actual, expected);
    }

}
