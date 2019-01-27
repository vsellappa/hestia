package hortonworks.hdf.streaming.view.druidmodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "topic",
    "replicas",
    "taskDuration",
    "completionTimeout",
    "consumerProperties"
})
public class IoConfig {

    @JsonProperty("topic")
    private String topic;
    @JsonProperty("replicas")
    private Integer replicas;
    @JsonProperty("taskDuration")
    private String taskDuration;
    @JsonProperty("completionTimeout")
    private String completionTimeout;
    @JsonProperty("consumerProperties")
    private ConsumerProperties consumerProperties;

    @JsonProperty("topic")
    public String getTopic() {
        return topic;
    }

    @JsonProperty("topic")
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @JsonProperty("replicas")
    public Integer getReplicas() {
        return replicas;
    }

    @JsonProperty("replicas")
    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    @JsonProperty("taskDuration")
    public String getTaskDuration() {
        return taskDuration;
    }

    @JsonProperty("taskDuration")
    public void setTaskDuration(String taskDuration) {
        this.taskDuration = taskDuration;
    }

    @JsonProperty("completionTimeout")
    public String getCompletionTimeout() {
        return completionTimeout;
    }

    @JsonProperty("completionTimeout")
    public void setCompletionTimeout(String completionTimeout) {
        this.completionTimeout = completionTimeout;
    }

    @JsonProperty("consumerProperties")
    public ConsumerProperties getConsumerProperties() {
        return consumerProperties;
    }

    @JsonProperty("consumerProperties")
    public void setConsumerProperties(ConsumerProperties consumerProperties) {
        this.consumerProperties = consumerProperties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(IoConfig.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("topic");
        sb.append('=');
        sb.append(((this.topic == null)?"<null>":this.topic));
        sb.append(',');
        sb.append("replicas");
        sb.append('=');
        sb.append(((this.replicas == null)?"<null>":this.replicas));
        sb.append(',');
        sb.append("taskDuration");
        sb.append('=');
        sb.append(((this.taskDuration == null)?"<null>":this.taskDuration));
        sb.append(',');
        sb.append("completionTimeout");
        sb.append('=');
        sb.append(((this.completionTimeout == null)?"<null>":this.completionTimeout));
        sb.append(',');
        sb.append("consumerProperties");
        sb.append('=');
        sb.append(((this.consumerProperties == null)?"<null>":this.consumerProperties));
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
        result = ((result* 31)+((this.topic == null)? 0 :this.topic.hashCode()));
        result = ((result* 31)+((this.completionTimeout == null)? 0 :this.completionTimeout.hashCode()));
        result = ((result* 31)+((this.consumerProperties == null)? 0 :this.consumerProperties.hashCode()));
        result = ((result* 31)+((this.taskDuration == null)? 0 :this.taskDuration.hashCode()));
        result = ((result* 31)+((this.replicas == null)? 0 :this.replicas.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof IoConfig) == false) {
            return false;
        }
        IoConfig rhs = ((IoConfig) other);
        return ((((((this.topic == rhs.topic)||((this.topic!= null)&&this.topic.equals(rhs.topic)))&&((this.completionTimeout == rhs.completionTimeout)||((this.completionTimeout!= null)&&this.completionTimeout.equals(rhs.completionTimeout))))&&((this.consumerProperties == rhs.consumerProperties)||((this.consumerProperties!= null)&&this.consumerProperties.equals(rhs.consumerProperties))))&&((this.taskDuration == rhs.taskDuration)||((this.taskDuration!= null)&&this.taskDuration.equals(rhs.taskDuration))))&&((this.replicas == rhs.replicas)||((this.replicas!= null)&&this.replicas.equals(rhs.replicas))));
    }

}
