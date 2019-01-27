package hortonworks.hdf.streaming.view.druidmodel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "dataSchema",
    "tuningConfig",
    "ioConfig"
})
public class USHousePricesSchema {

    @JsonProperty("type")
    private String type;
    @JsonProperty("dataSchema")
    private DataSchema dataSchema;
    @JsonProperty("tuningConfig")
    private TuningConfig tuningConfig;
    @JsonProperty("ioConfig")
    private IoConfig ioConfig;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("dataSchema")
    public DataSchema getDataSchema() {
        return dataSchema;
    }

    @JsonProperty("dataSchema")
    public void setDataSchema(DataSchema dataSchema) {
        this.dataSchema = dataSchema;
    }

    @JsonProperty("tuningConfig")
    public TuningConfig getTuningConfig() {
        return tuningConfig;
    }

    @JsonProperty("tuningConfig")
    public void setTuningConfig(TuningConfig tuningConfig) {
        this.tuningConfig = tuningConfig;
    }

    @JsonProperty("ioConfig")
    public IoConfig getIoConfig() {
        return ioConfig;
    }

    @JsonProperty("ioConfig")
    public void setIoConfig(IoConfig ioConfig) {
        this.ioConfig = ioConfig;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(USHousePricesSchema.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("dataSchema");
        sb.append('=');
        sb.append(((this.dataSchema == null)?"<null>":this.dataSchema));
        sb.append(',');
        sb.append("tuningConfig");
        sb.append('=');
        sb.append(((this.tuningConfig == null)?"<null>":this.tuningConfig));
        sb.append(',');
        sb.append("ioConfig");
        sb.append('=');
        sb.append(((this.ioConfig == null)?"<null>":this.ioConfig));
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
        result = ((result* 31)+((this.dataSchema == null)? 0 :this.dataSchema.hashCode()));
        result = ((result* 31)+((this.tuningConfig == null)? 0 :this.tuningConfig.hashCode()));
        result = ((result* 31)+((this.ioConfig == null)? 0 :this.ioConfig.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof USHousePricesSchema) == false) {
            return false;
        }
        USHousePricesSchema rhs = ((USHousePricesSchema) other);
        return (((((this.dataSchema == rhs.dataSchema)||((this.dataSchema!= null)&&this.dataSchema.equals(rhs.dataSchema)))&&((this.tuningConfig == rhs.tuningConfig)||((this.tuningConfig!= null)&&this.tuningConfig.equals(rhs.tuningConfig))))&&((this.ioConfig == rhs.ioConfig)||((this.ioConfig!= null)&&this.ioConfig.equals(rhs.ioConfig))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))));
    }

}
