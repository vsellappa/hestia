package hortonworks.hdf.streaming.view.druidmodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bootstrap.servers"
})
public class ConsumerProperties {

    @JsonProperty("bootstrap.servers")
    private String bootstrapServers;

    @JsonProperty("bootstrap.servers")
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    @JsonProperty("bootstrap.servers")
    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ConsumerProperties.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("bootstrapServers");
        sb.append('=');
        sb.append(((this.bootstrapServers == null)?"<null>":this.bootstrapServers));
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
        result = ((result* 31)+((this.bootstrapServers == null)? 0 :this.bootstrapServers.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConsumerProperties) == false) {
            return false;
        }
        ConsumerProperties rhs = ((ConsumerProperties) other);
        return ((this.bootstrapServers == rhs.bootstrapServers)||((this.bootstrapServers!= null)&&this.bootstrapServers.equals(rhs.bootstrapServers)));
    }

}
