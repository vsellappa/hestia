package hortonworks.hdf.streaming;

import net.minidev.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DataExtractorTest extends TestSuite {
    private Producer.DataExtractor dataExtractor;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        Producer p = new Producer(helper, new CountDownLatch(1));
        dataExtractor = p.new DataExtractor();
    }

    @Test(groups={"smokeTest"})
    public void testValidExternalAPIAccess() {
        List<JSONArray> val = dataExtractor.get("Z90210_MLPAH");
        Assert.assertTrue(val.size()!=0, "Data received from External Source");
    }

    @Test(groups={"smokeTest"})
    public void testInvalidExternalAPIAccess() {
        List<JSONArray> val = dataExtractor.get("test");
        Assert.assertEquals(val.size(), 0, "Exception handled and no data received");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        dataExtractor = null;
    }
}