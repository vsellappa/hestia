package hortonworks.hdf.streaming;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static net.sourceforge.argparse4j.impl.Arguments.store;

public class Application {
    private final static Helper helper = new Helper();
    private final static Logger LOGGER = LoggerFactory.getLogger("******* Hestia *******");

    private static String help() {
        return "/* ******************** Hestia ******************** */" +
               "TODO: Complete Me";
    }

    /**
     * Bootstrap the Application
     *
     * @param args
     * @throws Exception
     */
    public static void main(String... args) throws Exception{
        ArgumentParser parser = ArgumentParsers.newFor("Hestia").build().defaultHelp(true)
                .description(help());
        parser.addArgument("--bootstrap.servers").action(store()).required(false).type(String.class)
                .help("comma separated broker list");
        parser.addArgument("--file.location").action(store()).required(false).type(String.class)
                .help("location of the metadata file");

        LOGGER.info("Starting");

        if (helper.init(parser.parseArgs(args).getAttrs())) {
            final CountDownLatch latch = new CountDownLatch(2);

            final Producer producer = new Producer(helper, latch);
            final Consumer consumer = new Consumer(helper, latch);

            Runtime.getRuntime().addShutdownHook(new Thread(()-> {
                producer.stop();
                consumer.stop();

                LOGGER.info("Stopped All Producer and Consumer Threads");
            }));

            try {
                producer.start();
                consumer.start();

                latch.await();
            } catch (Exception x) {
                x.printStackTrace();
                System.exit(-1);
            }
        }

        LOGGER.info("Exit");
    }
}