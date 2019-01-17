package hortonworks.hdf.streaming;

import net.minidev.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DataExtractorTest extends TestSuite {
    private Producer.DataExtractor dataExtractor;

    @BeforeClass
    public void beforeClass() {
        Producer p = new Producer(helper, new CountDownLatch(1));
        dataExtractor = p.new DataExtractor();
    }

    @Test(groups={"systemCheck"})
    public void testValidExternalAPIAccess() {
        List<JSONArray> val = dataExtractor.get("Z90210_MLPAH");
        Assert.assertTrue(val.size()!=0, "Data received from External Source");
    }

    @Test(groups={"systemCheck"})
    public void testInvalidExternalAPIAccess() {
        List<JSONArray> val = dataExtractor.get("test");
        Assert.assertEquals(val.size(), 0, "Exception handled and no data received");
    }

    @AfterClass
    public void afterClass() {
        this.dataExtractor = null;
    }
}