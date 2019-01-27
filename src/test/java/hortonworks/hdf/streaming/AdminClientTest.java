package hortonworks.hdf.streaming;

import org.apache.kafka.clients.admin.AdminClient;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class AdminClientTest extends TestSuite {
    private AdminClient client;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        Properties p = new Properties();
        p.setProperty("bootstrap.servers", helper.getBootServers());

        client = AdminClient.create(p);
    }

    @Test(groups={"smokeTest"})
    public void checkTopics() throws InterruptedException, ExecutionException {
        final List<String> topicNames = helper.getTopicNames();
        Set<String> clients = client.listTopics().names().get();

        topicNames.forEach(name -> {
            Assert.assertTrue(clients.contains(name), "Topic Exists in Kafka");
        });
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        client = null;
    }
}
