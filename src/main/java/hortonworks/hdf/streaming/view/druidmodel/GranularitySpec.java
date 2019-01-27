package hortonworks.hdf.streaming.view.druidmodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "segmentGranularity",
    "queryGranularity",
    "rollup"
})
public class GranularitySpec {

    @JsonProperty("type")
    private String type;
    @JsonProperty("segmentGranularity")
    private String segmentGranularity;
    @JsonProperty("queryGranularity")
    private String queryGranularity;
    @JsonProperty("rollup")
    private Boolean rollup;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("segmentGranularity")
    public String getSegmentGranularity() {
        return segmentGranularity;
    }

    @JsonProperty("segmentGranularity")
    public void setSegmentGranularity(String segmentGranularity) {
        this.segmentGranularity = segmentGranularity;
    }

    @JsonProperty("queryGranularity")
    public String getQueryGranularity() {
        return queryGranularity;
    }

    @JsonProperty("queryGranularity")
    public void setQueryGranularity(String queryGranularity) {
        this.queryGranularity = queryGranularity;
    }

    @JsonProperty("rollup")
    public Boolean getRollup() {
        return rollup;
    }

    @JsonProperty("rollup")
    public void setRollup(Boolean rollup) {
        this.rollup = rollup;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(GranularitySpec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("segmentGranularity");
        sb.append('=');
        sb.append(((this.segmentGranularity == null)?"<null>":this.segmentGranularity));
        sb.append(',');
        sb.append("queryGranularity");
        sb.append('=');
        sb.append(((this.queryGranularity == null)?"<null>":this.queryGranularity));
        sb.append(',');
        sb.append("rollup");
        sb.append('=');
        sb.append(((this.rollup == null)?"<null>":this.rollup));
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
        result = ((result* 31)+((this.segmentGranularity == null)? 0 :this.segmentGranularity.hashCode()));
        result = ((result* 31)+((this.queryGranularity == null)? 0 :this.queryGranularity.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.rollup == null)? 0 :this.rollup.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GranularitySpec) == false) {
            return false;
        }
        GranularitySpec rhs = ((GranularitySpec) other);
        return (((((this.segmentGranularity == rhs.segmentGranularity)||((this.segmentGranularity!= null)&&this.segmentGranularity.equals(rhs.segmentGranularity)))&&((this.queryGranularity == rhs.queryGranularity)||((this.queryGranularity!= null)&&this.queryGranularity.equals(rhs.queryGranularity))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.rollup == rhs.rollup)||((this.rollup!= null)&&this.rollup.equals(rhs.rollup))));
    }

}
