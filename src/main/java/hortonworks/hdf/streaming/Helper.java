package hortonworks.hdf.streaming;

import net.sourceforge.argparse4j.inf.Namespace;
import java.util.*;

public class Helper {
    private final Properties properties = new Properties();
    private final List<String> topicNames = Arrays.asList("Metadata","Data");

    final boolean init(Namespace result) throws Exception {
        properties.load(getClass().getResourceAsStream("/application.properties"));

        //overwrite if present on commandline
        setBootServers(result);
        setFileLocation(result);

        return true;
    }

    final String getBootServers() { return properties.getProperty("bootstrap.servers");}
    final String getFileLocation() { return properties.getProperty("file.location");}

    final ArrayList<String> getTopicNames() {
        ArrayList<String> names = new ArrayList<>(topicNames.size());
        names.addAll(topicNames);
        return names;
    }

    private void setBootServers(Namespace result) {
        String s = result.getString("bootstrap.servers");
        if (s!= null && !s.isEmpty())
            properties.setProperty("bootstrap.servers", s);
    }

    private void setFileLocation(Namespace result) {
        String s = result.getString("file.location");
        if (s!= null && !s.isEmpty())
            properties.setProperty("file.location", s);

    }
}
