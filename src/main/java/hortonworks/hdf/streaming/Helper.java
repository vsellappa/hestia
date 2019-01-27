package hortonworks.hdf.streaming;

import java.util.*;

public class Helper {
    private final Properties properties = new Properties();
    private final Map<String, Object> cmdLineArgs = new HashMap<String, Object>();
    private final List<String> topicNames = Arrays.asList("Lookup","TxnData", "USHousePrices");

    public Helper(String fileName) throws Exception {
        properties.load(getClass().getResourceAsStream(fileName));
    }

    final boolean init(Map<String, Object> args) {
        cmdLineArgs.putAll(args);

        //overwrite if present on commandline
        setBootServers();
        setFileLocation();
        setApiKey();

        setDruidServer();
        setDruidSchema();

        return true;
    }

    public final String getBootServers() { return properties.getProperty("bootstrap.servers");}
    public final String getFileLocation() { return properties.getProperty("file.location");}
    public final String getApiKey() { return properties.getProperty("api.key");}
    public final String getDruidServer() { return properties.getProperty("druid.server");}
    public final String getDruidSchema() { return properties.getProperty("druid.schema");}

    public final List<String> getTopicNames() {
        return Collections.unmodifiableList(topicNames);
    }

    private void setBootServers() {setValue("bootstrap.servers");}
    private void setFileLocation() {setValue("file.location");}
    private void setApiKey() {setValue("api.key");}
    private void setDruidServer() {setValue("druid.server");}
    private void setDruidSchema() {setValue("druid.schema");}

    private void setValue(String key) {
        Object o = cmdLineArgs.get(key);
        if (o!=null) {
            String s = o.toString();
            if (s != null && !s.isEmpty()) properties.setProperty(key, s);
        }
    }
}