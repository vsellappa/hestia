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
        setApiKey(result);

        return true;
    }

    final String getBootServers() { return properties.getProperty("bootstrap.servers");}
    final String getFileLocation() { return properties.getProperty("file.location");}
    final String getApiKey() { return properties.getProperty("api.key");}

    final ArrayList<String> getTopicNames() {
        ArrayList<String> names = new ArrayList<>(topicNames.size());
        names.addAll(topicNames);
        return names;
    }

    private void setBootServers(Namespace result) {setValue("bootstrap.servers", result);}
    private void setFileLocation(Namespace result) {setValue("file.location", result);}
    private void setApiKey(Namespace result) {setValue("api.key", result);}

    private void setValue(String name, Namespace result) {
        String s = result.getString(name);
        if (s!=null && !s.isEmpty()) properties.setProperty(name,s);
    }
}