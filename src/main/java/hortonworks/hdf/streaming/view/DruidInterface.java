package hortonworks.hdf.streaming.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import hortonworks.hdf.streaming.Helper;
import hortonworks.hdf.streaming.view.druidmodel.USHousePricesSchema;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class DruidInterface {
    private final Helper helper;
    private final String baseURI="/druid/indexer/v1/supervisor";

    private final static Logger LOGGER = LoggerFactory.getLogger("DruidInterface");

    public DruidInterface(Helper helper) {
        this.helper = helper;
    }

    public String enable() throws Exception {
        try (InputStream is = getClass().getResourceAsStream(helper.getDruidSchema())) {
            if (null == is) throw new RuntimeException("Error : Unable to get Schema for Druid");

            final ObjectMapper mapper = new ObjectMapper();
            USHousePricesSchema schema = mapper.readValue(is, USHousePricesSchema.class);

            setBootStrapServer(schema);
            return submitSpec(mapper.writeValueAsString(schema));
        }
    }

    public String terminate(String id) throws Exception {
        if (id != null && !id.isEmpty()) {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost p = new HttpPost(helper.getDruidServer() + baseURI + "/" + id + "/terminate");
                CloseableHttpResponse response = client.execute(p);

                LOGGER.info("Terminating Supervisor: " + id);

                return JsonPath.parse(EntityUtils.toString(response.getEntity())).read("id");
            }
        }

        return null;
    }

    public List<String> getId() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet g = new HttpGet(helper.getDruidServer() + baseURI);
            CloseableHttpResponse response = client.execute(g);

            return Collections.unmodifiableList(JsonPath.parse(EntityUtils.toString(response.getEntity())).read("$"));
        }
    }

    private String submitSpec(String schema) throws Exception {
        LOGGER.debug("Submitting Schema to Druid: " + schema);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost p = new HttpPost(helper.getDruidServer() + baseURI);
            p.setEntity(new StringEntity(schema, ContentType.APPLICATION_JSON));

            CloseableHttpResponse response = client.execute(p);
            HttpEntity entity = response.getEntity();
            StatusLine status = response.getStatusLine();

            if (status.getStatusCode()!=200) {
                StringBuilder message = new StringBuilder();
                if (entity!=null) {message.append(EntityUtils.toString(entity));}

                throw new HttpResponseException(status.getStatusCode(),status.getReasonPhrase()+"|"+message);
            }

            String s = EntityUtils.toString(entity);
            LOGGER.info("Submitted Schema to Druid: " + status.getStatusCode() +"|" + s);

            return JsonPath.parse(s).read("id");
        }
    }

    private void setBootStrapServer(USHousePricesSchema schema) {
        schema.getIoConfig().getConsumerProperties().setBootstrapServers(helper.getBootServers());
    }
}