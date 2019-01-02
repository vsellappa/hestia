package hortonworks.hdf.streaming;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

public class Consumer {
    private final Helper helper;
    private final CountDownLatch latch;

    private KafkaStreams streams;

    private final static String TOPICNAME = "USHousePrices";
    private final static JSONArray ARRAYNAMES = new JSONArray("[code,name,description,refreshedAt,fromDate,toDate,date,value]");
    private final static Logger LOGGER = LoggerFactory.getLogger("Consumer");

    public Consumer(Helper helper, CountDownLatch latch) {
        this.helper = helper;
        this.latch = latch;
    }

    public final void start() {
        streams = init();
        streams.start();
    }

    public final void stop() {
        streams.close();
        latch.countDown();

        LOGGER.info("Closing Streams...");
    }

    private final KafkaStreams init() {
        final StreamsBuilder builder = new StreamsBuilder();

        //create a state store from the metadata topic
        final KTable<String, String> metadata = builder.table (helper.getTopicNames().get(0)
                ,Materialized.<String,String,KeyValueStore<Bytes, byte[]>> as("metadataTable").withKeySerde(Serdes.String()).withValueSerde(Serdes.String()));

        //create a stream from the data topic
        final KStream<String, String> transactions = builder.stream (helper.getTopicNames().get(1)
                ,Consumed.with(Serdes.String(),Serdes.String())
        );

        ValueJoiner<String, String, String> valueJoiner = new ValueJoiner<String, String, String>() {
            @Override
            public String apply(String key, String lookup) {
                String[] splits = key.split(Pattern.quote("|"));
                JSONObject jsonObject = CDL.rowToJSONObject(ARRAYNAMES, new JSONTokener(lookup + "," + splits[0] + "," + splits[1]));

                LOGGER.debug("Writing to Topic : " + TOPICNAME + " Data=" + jsonObject);

                return jsonObject.toString();
            }
        };

        //join the stream with the lookup table and write to a separate stream.
        final KStream<String, String> joined = transactions.join(metadata,valueJoiner);
        joined.to(TOPICNAME);

        LOGGER.info("Created Joined Stream here: " + joined);

        return new KafkaStreams(builder.build(),getConfig());
    }

    private final Properties getConfig() {
        Properties p = new Properties();
        p.put("application.id",  "hestia");
        p.put("bootstrap.servers", helper.getBootServers());
        p.put("auto.offset.reset", "earliest");
        p.put("commit.interval.ms",500);
        p.put("default.key.serde", Serdes.String().getClass());
        p.put("default.value.serde", Serdes.String().getClass());

        return p;
    }
}