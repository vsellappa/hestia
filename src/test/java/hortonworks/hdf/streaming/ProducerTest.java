package hortonworks.hdf.streaming;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.util.Properties;

public class ProducerTest {

    /**
     * Write a record that does not have a corresponding data record.
     * Check if the consumer picks that up.
     */
    @Test
    public void check() {
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
        ProducerRecord<String, String> record = new ProducerRecord<>("Metadata","Z90210_MLP3B","Test Record");

        kProducer.send(record);
        kProducer.close();
    }
}
