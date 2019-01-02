package hortonworks.hdf.streaming;

import net.minidev.json.JSONArray;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DataExtractorTest {
    private Producer.DataExtractor dataExtractor;

    @Before
    public void setUp() {
        Producer p = new Producer(new Helper(), new CountDownLatch(1));
        dataExtractor = p.new DataExtractor();
    }

    @Test
    public void testJsonPath() {
        List<JSONArray> val = dataExtractor.get("Z90210_MLPAH");
        Assert.assertNotNull(val);

        Assert.assertEquals(39, val.size());
    }

    @After
    public void tearDown() {
        this.dataExtractor = null;
    }
}