package hortonworks.hdf.streaming.view.druidmodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "format",
    "timestampSpec",
    "dimensionsSpec"
})
public class ParseSpec {

    @JsonProperty("format")
    private String format;
    @JsonProperty("timestampSpec")
    private TimestampSpec timestampSpec;
    @JsonProperty("dimensionsSpec")
    private DimensionsSpec dimensionsSpec;

    @JsonProperty("format")
    public String getFormat() {
        return format;
    }

    @JsonProperty("format")
    public void setFormat(String format) {
        this.format = format;
    }

    @JsonProperty("timestampSpec")
    public TimestampSpec getTimestampSpec() {
        return timestampSpec;
    }

    @JsonProperty("timestampSpec")
    public void setTimestampSpec(TimestampSpec timestampSpec) {
        this.timestampSpec = timestampSpec;
    }

    @JsonProperty("dimensionsSpec")
    public DimensionsSpec getDimensionsSpec() {
        return dimensionsSpec;
    }

    @JsonProperty("dimensionsSpec")
    public void setDimensionsSpec(DimensionsSpec dimensionsSpec) {
        this.dimensionsSpec = dimensionsSpec;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ParseSpec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("format");
        sb.append('=');
        sb.append(((this.format == null)?"<null>":this.format));
        sb.append(',');
        sb.append("timestampSpec");
        sb.append('=');
        sb.append(((this.timestampSpec == null)?"<null>":this.timestampSpec));
        sb.append(',');
        sb.append("dimensionsSpec");
        sb.append('=');
        sb.append(((this.dimensionsSpec == null)?"<null>":this.dimensionsSpec));
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
        result = ((result* 31)+((this.format == null)? 0 :this.format.hashCode()));
        result = ((result* 31)+((this.dimensionsSpec == null)? 0 :this.dimensionsSpec.hashCode()));
        result = ((result* 31)+((this.timestampSpec == null)? 0 :this.timestampSpec.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ParseSpec) == false) {
            return false;
        }
        ParseSpec rhs = ((ParseSpec) other);
        return ((((this.format == rhs.format)||((this.format!= null)&&this.format.equals(rhs.format)))&&((this.dimensionsSpec == rhs.dimensionsSpec)||((this.dimensionsSpec!= null)&&this.dimensionsSpec.equals(rhs.dimensionsSpec))))&&((this.timestampSpec == rhs.timestampSpec)||((this.timestampSpec!= null)&&this.timestampSpec.equals(rhs.timestampSpec))));
    }

}
