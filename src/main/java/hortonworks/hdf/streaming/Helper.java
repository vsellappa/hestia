package hortonworks.hdf.streaming;

import java.util.*;

public class Helper {
    private final Properties properties = new Properties();
    private final Map<String, Object> cmdLineArgs = new HashMap<String, Object>();
    private final List<String> topicNames = Arrays.asList("Lookup","TxnData", "USHousePrices");

    final boolean init(Map<String, Object> args) throws Exception {
        cmdLineArgs.putAll(args);
        properties.load(getClass().getResourceAsStream("/application.properties"));

        //overwrite if present on commandline
        setBootServers();
        setFileLocation();
        setApiKey();

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

    private void setBootServers() {setValue("bootstrap.servers");}
    private void setFileLocation() {setValue("file.location");}
    private void setApiKey() {setValue("api.key");}

    private void setValue(String key) {
        Object o = cmdLineArgs.get(key);
        if (o!=null) {
            String s = o.toString();
            if (s != null && !s.isEmpty()) properties.setProperty(key, s);
        }
    }
}