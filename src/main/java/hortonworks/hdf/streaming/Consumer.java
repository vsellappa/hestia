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

    private final static JSONArray ARRAYNAMES = new JSONArray("[timestamp,code,name,description,refreshedAt,fromDate,toDate,date,value]");
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
        if (null!=streams) streams.close();
        latch.countDown();

        LOGGER.info("Closing Streams...");
    }

    private final KafkaStreams init() {
        final StreamsBuilder builder = new StreamsBuilder();

        final KTable<String, String> lookup = builder.table (helper.getTopicNames().get(0)
                ,Materialized.<String,String,KeyValueStore<Bytes, byte[]>> as("lookup")
                 .withKeySerde(Serdes.String())
                 .withValueSerde(Serdes.String()));

        final KStream<String, String> transactions = builder.stream (helper.getTopicNames().get(1)
                ,Consumed.with(Serdes.String(),Serdes.String())
        );

        //TODO: Create SerDe and use proper JSONObject's here.
        ValueJoiner<String, String, String> valueJoiner = new ValueJoiner<String, String, String>() {
            @Override
            public String apply(String key, String lookup) {
                String[] splits = key.split(Pattern.quote("|"));
                JSONObject jsonObject = CDL.rowToJSONObject(ARRAYNAMES, new JSONTokener(System.currentTimeMillis() + "," + lookup + "," + splits[0] + "," + splits[1]));

                return jsonObject.toString();
            }
        };

        //join the stream with the lookup table and write to a separate stream.
        final KStream<String, String> result = transactions.join(lookup,valueJoiner);

        //write the resulting stream to a separate topic
        //TODO: Does this stream have to be grouped by key.
        result.to(helper.getTopicNames().get(2));

        LOGGER.info("Result Stream Created : " + result);
        result.foreach((k, v) -> LOGGER.debug("Key=:" + k + " Value=" + v));

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