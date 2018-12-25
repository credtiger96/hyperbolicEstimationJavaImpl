import org.ejml.simple.SimpleMatrix;
import org.junit.Assert;
import org.junit.Test;

public class HyperbolicEstimationChansTest {
    @Test
    public void testMain () {
        SimpleMatrix ToAs = new SimpleMatrix(5,4, true, new double[] {
                20.0, -45.0, -10.0, 56.23166368,
                0.0, -22.0, 20.0, 32.54228019,
                0.0, 0.0, -50.0, 57.85326266,
                0.0, 36.0, 10.0, 29.9833287,
                -36.0, 30.0, 0.0, 49.26459175
        });
        SimpleMatrix res = HyperbolicEstimationChans.performForMany(ToAs);

        Assert.assertEquals(res.get(0, 0), 7., 0.01);
        Assert.assertEquals(res.get(1, 0), 7., 0.01);
        Assert.assertEquals(res.get(2, 0), 7., 0.01);
    }
}
