package hortonworks.hdf.streaming;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.util.HashMap;
import java.util.Map;

public class TestSuite {
    final Helper helper = new Helper();
    final Map<String, Object> params = new HashMap<>();

    @Parameters({"bootstrap.servers", "api.key", "file.location"})
    @BeforeSuite
    public void beforeSuite(String bootstrapServers,String apiKey, String fileLocation) throws Exception {
        params.put("bootstrap.servers", bootstrapServers);
        params.put("api.key", apiKey);
        params.put("file.location", fileLocation);

        helper.init(params);
    }

    @AfterSuite
    public void afterSuite() {

    }
}
