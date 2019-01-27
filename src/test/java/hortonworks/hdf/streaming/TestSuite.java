package hortonworks.hdf.streaming;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class TestSuite {
    public static Helper helper;
    public final Map<String, Object> cmdLineArgs = new HashMap<>();

    @Parameters({"bootstrap.servers", "api.key", "file.location", "druid.server", "druid.schema"})
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(@Optional String bootstrapServers
            , @Optional String apiKey
            , @Optional String fileLocation
            , @Optional String druidServer
            , @Optional String druidSchema) throws Exception{

        helper = new Helper("/testApplication.properties");

        cmdLineArgs.put("bootstrap.servers", bootstrapServers);
        cmdLineArgs.put("api.key", apiKey);
        cmdLineArgs.put("file.location", fileLocation);

        cmdLineArgs.put("druid.server", druidServer);
        cmdLineArgs.put("druid.schema", druidSchema);

        //will overwrite if present on commandline
        helper.init(Collections.unmodifiableMap(cmdLineArgs));
    }
}
