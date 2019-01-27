package hortonworks.hdf.streaming.view;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import hortonworks.hdf.streaming.TestSuite;
import hortonworks.hdf.streaming.view.druidmodel.USHousePricesSchema;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class DruidInterfaceTest extends TestSuite implements Closeable {
    private InputStream is = null;
    private DruidInterface druidInterface;
    private String id;

    @BeforeClass(alwaysRun=true)
    public void beforeClass() {
        is = getClass().getResourceAsStream("/data/testDruidSchema.json");
        Assert.assertNotNull(is);
    }

    @Test(groups="smokeTest")
    public void testSubmission() throws Exception {
        druidInterface = new DruidInterface(helper);
        id = druidInterface.enable();

        Assert.assertNotNull(id, "Supervisor Unique ID");
    }

    @Test(groups="smokeTest", dependsOnMethods = {"testSubmission"})
    public void getId() throws Exception {
        Set<String> s = new HashSet<>(druidInterface.getId());
        Assert.assertTrue(s.contains(id), "ID retrieved from Druid");
    }

    @Test(groups="smokeTest", dependsOnMethods = {"getId"})
    public void terminate() throws Exception {
        Assert.assertNotNull(druidInterface.terminate(id));
    }

    @Test(groups="unitTest")
    public void testJsonParse() {
        String r = "{" + "\"id\"" + ":" + "\"USHousePrices\"" + "}";
        String p = JsonPath.parse(r).read("id");

        Assert.assertEquals(p, "USHousePrices", "Id returns USHousePrices");
    }

    @Test(groups = "unitTest")
    public void testSchemaMapping() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        USHousePricesSchema schema = mapper.readValue(is, USHousePricesSchema.class);

        Assert.assertNotNull(schema, "Auto mapping should succeed");

        Assert.assertEquals(schema.getType(),"kafka", "The defined type is Kafka");
        Assert.assertEquals(schema.getIoConfig().getConsumerProperties().getBootstrapServers(), "localhost:9092", "Bootstrap server defined as ocalhost:9092 in test message");

        Assert.assertNotNull(mapper.writeValueAsString(schema));
    }

    @Test(groups = "unitTest")
    public void dateTimeFormat() {
        Instant instant = Instant.now();
        System.out.println(DateTimeFormatter.ISO_INSTANT.format(instant));
    }

    @Override
    public void close() throws IOException {
        is.close();
    }
}