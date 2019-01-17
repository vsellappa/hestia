package hortonworks.hdf.streaming;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class TransactionDataTest {
    public void checkIfConsumerPicksThisUp() {
        Properties p = new Properties();

        p.put("bootstrap.servers", "localhost:9092");
        p.put("acks", "all");
        p.put("delivery.timeout.ms", 30000);
        p.put("batch.size", 16384);
        p.put("linger.ms", 1);
        p.put("buffer.memory", 33554432);
        p.put("request.timeout.ms", 1000);
        p.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        p.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> kProducer = new KafkaProducer(p);
        ProducerRecord<String, String> record = new ProducerRecord<>("Data","Z90210_MLP3B","2019-01-05|200000.00");

        kProducer.send(record);
        kProducer.close();

    }
}
