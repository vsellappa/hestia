package hortonworks.hdf.streaming.view.druidmodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "parseSpec"
})
public class Parser {

    @JsonProperty("type")
    private String type;
    @JsonProperty("parseSpec")
    private ParseSpec parseSpec;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("parseSpec")
    public ParseSpec getParseSpec() {
        return parseSpec;
    }

    @JsonProperty("parseSpec")
    public void setParseSpec(ParseSpec parseSpec) {
        this.parseSpec = parseSpec;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Parser.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("parseSpec");
        sb.append('=');
        sb.append(((this.parseSpec == null)?"<null>":this.parseSpec));
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
        result = ((result* 31)+((this.parseSpec == null)? 0 :this.parseSpec.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Parser) == false) {
            return false;
        }
        Parser rhs = ((Parser) other);
        return (((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type)))&&((this.parseSpec == rhs.parseSpec)||((this.parseSpec!= null)&&this.parseSpec.equals(rhs.parseSpec))));
    }

}
