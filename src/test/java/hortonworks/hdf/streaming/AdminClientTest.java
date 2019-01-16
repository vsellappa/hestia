package hortonworks.hdf.streaming;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class AdminClientTest {
    private AdminClient client;
    private Helper helper = new Helper();

    @Before
    private void setUp() {
        client = AdminClient.create(getConfig());
    }

    @Test
    private void listTopics() throws InterruptedException, ExecutionException {
        final ArrayList<String> topicNames = helper.getTopicNames();
        Set<String> clients = client.listTopics().names().get();

        Assert.assertEquals("All topics present in Kafka",topicNames.size(),clients.size());
    }

    @After
    private void tearDown() {
        client.close();
    }

    private final Properties getConfig() {
        Properties p = new Properties();
        p.put("bootstrap.servers", helper.getBootServers());

        return p;
    }
}
