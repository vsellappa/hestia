package hortonworks.hdf.streaming;

import org.apache.kafka.clients.admin.AdminClient;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class AdminClientTest extends TestSuite {
    private AdminClient client;

    @BeforeClass
    public void beforeClass() {
        Properties p = new Properties();
        p.setProperty("bootstrap.servers", helper.getBootServers());

        client = AdminClient.create(p);
    }

    @Test(groups={"systemCheck"})
    public void checkTopics() throws InterruptedException, ExecutionException {
        final ArrayList<String> topicNames = helper.getTopicNames();
        Set<String> clients = client.listTopics().names().get();

        topicNames.forEach(name -> {
            Assert.assertTrue(clients.contains(name), "Topic Exists in Kafka");
        });
    }

    @AfterClass
    public void afterClass() {
        client.close();
    }
}
