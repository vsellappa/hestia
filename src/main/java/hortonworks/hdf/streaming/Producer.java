package hortonworks.hdf.streaming;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Producer {
    private final Helper helper;
    private final CountDownLatch latch;

    private final static Logger LOGGER = LoggerFactory.getLogger("Producer");

    public Producer(Helper helper, CountDownLatch latch) {
        this.helper = helper;
        this.latch = latch;
    }

    public void start() {
        LOGGER.info("Writing Data To Topics....");
        getWriters().forEach(writer -> new Thread(writer, writer.getName()).start());
    }

    public void stop() {
        LOGGER.info("Stopping Writers....");
        getWriters().forEach(writer -> writer.stop());
    }

    private List<Writers> getWriters() {
        ArrayList<Writers> writers = new ArrayList<>();

        writers.add(new MetadataWriter(latch));
        writers.add(new TransactionDataWriter(latch));

        return Collections.unmodifiableList(writers);
    }

    interface Writers extends Runnable {
        String getName();
        void stop();
    }

    abstract class DataWriter implements Writers {
        private final CountDownLatch latch;
        protected final KafkaProducer<String, String> kProducer;

        protected DataWriter(CountDownLatch latch) {
            this.latch = latch;
            this.kProducer = new KafkaProducer<>(getConfig());
        }

        @Override
        public void run() {
            try {
                InputStream is = getClass().getResourceAsStream(helper.getFileLocation());
                Scanner scanner = new Scanner(is, "UTF-8");
                scanner.nextLine(); //ignore the header

                while (scanner.hasNextLine()) {
                    process(scanner.nextLine());
                }
            } catch (Exception x) {
                x.printStackTrace();
                LOGGER.error("Error In Data Ingestion : " + x.getMessage());
            }
        }

        @Override
        public void stop() {
            kProducer.flush();
            kProducer.close();

            LOGGER.debug("Closed Kafka Producer: " + kProducer.toString());
            latch.countDown();
        }

        protected abstract void process(String line);

        private Properties getConfig() {
            Properties p = new Properties();

            p.put("bootstrap.servers", helper.getBootServers());
            p.put("acks", "all");
            p.put("delivery.timeout.ms", 30000);
            p.put("batch.size", 16384);
            p.put("linger.ms", 1);
            p.put("buffer.memory", 33554432);
            p.put("request.timeout.ms", 1000);
            p.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            p.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

            return p;
        }
    }

    class MetadataWriter extends DataWriter {
        private MetadataWriter(CountDownLatch latch) {
            super(latch);
        }

        @Override
        protected void process(String line) {
            String topicName = helper.getTopicNames().get(0);

            LOGGER.debug("Key:|" + line.split(",")[0] + "| Value: |" + line + "|");
            kProducer.send(new ProducerRecord<>(topicName, line.split(",")[0], line));
        }

        @Override
        public String getName() {
            return "Producer$MetadataWriter";
        }
    }

    class TransactionDataWriter extends DataWriter {
        private final DataExtractor extractor = new DataExtractor();

        private TransactionDataWriter(CountDownLatch latch) {
            super(latch);
        }

        @Override
        protected void process(String line) {
            String topicName = helper.getTopicNames().get(1);
            String code = line.split(",")[0];

            LOGGER.debug("Getting data for Code : " + code);
            List<JSONArray> val = extractor.get(code);

            for (JSONArray array : val) {
                String date = array.get(0).toString();
                String value = array.get(1).toString();

                kProducer.send(new ProducerRecord<>(topicName, code, date + "|" + value));
            }
        }

        @Override
        public String getName() {
            return "Producer$TransactionDataWriter";
        }
    }

    class DataExtractor {
        private final String baseURI = "https://www.quandl.com/api/v3/datasets/ZILLOW/";
        private final String apiKey = helper.getApiKey();

        protected final List<JSONArray> get(String code) {
            HttpClient client = HttpClients.createDefault();
            final List<JSONArray> val = new ArrayList<>();

            HttpGet g = new HttpGet(baseURI + code + ".json?api_key=" + apiKey);
            LOGGER.info("Sending Request To: " + g.getRequestLine());

            try {
                val.addAll(client.execute(g, (response -> {
                    StatusLine status = response.getStatusLine();
                    HttpEntity entity = response.getEntity();

                    if (status.getStatusCode()!=200) {
                        StringBuilder message = new StringBuilder();
                        if (entity!=null) {message.append(EntityUtils.toString(entity));}

                        throw new HttpResponseException(status.getStatusCode(),status.getReasonPhrase()+"|"+message);
                    }

                    return (JsonPath.parse(EntityUtils.toString(response.getEntity())).read("$['dataset']['data'][*]"));
                })));
            } catch (HttpResponseException x) {
                LOGGER.warn("Exception When Retrieving Data for:|" + code + "| Exception:" + x.getStatusCode() + " " + x.getMessage());
            } catch (IOException x) {
                LOGGER.warn("Exception When Retrieving Data for:|" + code + "| Exception:" + x.getMessage());
            }

            return Collections.unmodifiableList(val);
        }
    }
}