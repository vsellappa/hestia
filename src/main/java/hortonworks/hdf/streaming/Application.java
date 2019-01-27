package hortonworks.hdf.streaming;

import hortonworks.hdf.streaming.view.DruidInterface;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

import static net.sourceforge.argparse4j.impl.Arguments.store;

public class Application {
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
        parser.addArgument("--bootstrap.servers").action(store()).required(true).type(String.class)
                .help("Comma separated Kafka broker list");
        parser.addArgument("--file.location").action(store()).required(false).type(String.class)
                .help("Location of the Lookup/Metadata file , must be on the classpath");
        parser.addArgument("--api.key").action(store()).required(false).type(String.class)
                .help("API Key for Quandl");
        parser.addArgument("--druid.server").action(store()).required(false).type(String.class)
                .help("Name and Port of the Druid Supervisor like localhost:8081");
        parser.addArgument("--druid.schema").action(store()).required(false).type(String.class)
                .help("JSON Schema location for the Druid Supervisor");

        LOGGER.info("Starting");

        final Helper helper = new Helper("/application.properties");

        if (helper.init(parser.parseArgs(args).getAttrs())) {
            final CountDownLatch latch = new CountDownLatch(2);

            final Producer producer = new Producer(helper, latch);
            final Consumer consumer = new Consumer(helper, latch);

            final DruidInterface druidInterface = new DruidInterface(helper);

            Runtime.getRuntime().addShutdownHook(new Thread(()-> {
                producer.stop();
                consumer.stop();

                LOGGER.info("Stopped All Producer and Consumer Threads");
            }));

            try {

                /*
                TODO: replace this with Tranquility Server API.
                druidInterface.enable();
                */
                producer.start();
                consumer.start();

                latch.await();
            } catch (Exception x) {
                x.printStackTrace();
                LOGGER.error("Error on startup : " + x.getMessage());
                System.exit(-1);
            }
        }

        LOGGER.info("Exit");
    }
}