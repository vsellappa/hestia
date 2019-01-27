package hortonworks.hdf.streaming.view.druidmodel;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dataSource",
    "parser",
    "metricsSpec",
    "granularitySpec"
})
public class DataSchema {

    @JsonProperty("dataSource")
    private String dataSource;
    @JsonProperty("parser")
    private Parser parser;
    @JsonProperty("metricsSpec")
    private List<Object> metricsSpec = new ArrayList<Object>();
    @JsonProperty("granularitySpec")
    private GranularitySpec granularitySpec;

    @JsonProperty("dataSource")
    public String getDataSource() {
        return dataSource;
    }

    @JsonProperty("dataSource")
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    @JsonProperty("parser")
    public Parser getParser() {
        return parser;
    }

    @JsonProperty("parser")
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    @JsonProperty("metricsSpec")
    public List<Object> getMetricsSpec() {
        return metricsSpec;
    }

    @JsonProperty("metricsSpec")
    public void setMetricsSpec(List<Object> metricsSpec) {
        this.metricsSpec = metricsSpec;
    }

    @JsonProperty("granularitySpec")
    public GranularitySpec getGranularitySpec() {
        return granularitySpec;
    }

    @JsonProperty("granularitySpec")
    public void setGranularitySpec(GranularitySpec granularitySpec) {
        this.granularitySpec = granularitySpec;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DataSchema.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("dataSource");
        sb.append('=');
        sb.append(((this.dataSource == null)?"<null>":this.dataSource));
        sb.append(',');
        sb.append("parser");
        sb.append('=');
        sb.append(((this.parser == null)?"<null>":this.parser));
        sb.append(',');
        sb.append("metricsSpec");
        sb.append('=');
        sb.append(((this.metricsSpec == null)?"<null>":this.metricsSpec));
        sb.append(',');
        sb.append("granularitySpec");
        sb.append('=');
        sb.append(((this.granularitySpec == null)?"<null>":this.granularitySpec));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.metricsSpec == null)? 0 :this.metricsSpec.hashCode()));
        result = ((result* 31)+((this.parser == null)? 0 :this.parser.hashCode()));
        result = ((result* 31)+((this.dataSource == null)? 0 :this.dataSource.hashCode()));
        result = ((result* 31)+((this.granularitySpec == null)? 0 :this.granularitySpec.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DataSchema) == false) {
            return false;
        }
        DataSchema rhs = ((DataSchema) other);
        return (((((this.metricsSpec == rhs.metricsSpec)||((this.metricsSpec!= null)&&this.metricsSpec.equals(rhs.metricsSpec)))&&((this.parser == rhs.parser)||((this.parser!= null)&&this.parser.equals(rhs.parser))))&&((this.dataSource == rhs.dataSource)||((this.dataSource!= null)&&this.dataSource.equals(rhs.dataSource))))&&((this.granularitySpec == rhs.granularitySpec)||((this.granularitySpec!= null)&&this.granularitySpec.equals(rhs.granularitySpec))));
    }

}
