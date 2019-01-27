package hortonworks.hdf.streaming.view.druidmodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "reportParseExceptions"
})
public class TuningConfig {

    @JsonProperty("type")
    private String type;
    @JsonProperty("reportParseExceptions")
    private Boolean reportParseExceptions;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("reportParseExceptions")
    public Boolean getReportParseExceptions() {
        return reportParseExceptions;
    }

    @JsonProperty("reportParseExceptions")
    public void setReportParseExceptions(Boolean reportParseExceptions) {
        this.reportParseExceptions = reportParseExceptions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TuningConfig.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("reportParseExceptions");
        sb.append('=');
        sb.append(((this.reportParseExceptions == null)?"<null>":this.reportParseExceptions));
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
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.reportParseExceptions == null)? 0 :this.reportParseExceptions.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TuningConfig) == false) {
            return false;
        }
        TuningConfig rhs = ((TuningConfig) other);
        return (((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type)))&&((this.reportParseExceptions == rhs.reportParseExceptions)||((this.reportParseExceptions!= null)&&this.reportParseExceptions.equals(rhs.reportParseExceptions))));
    }

}
