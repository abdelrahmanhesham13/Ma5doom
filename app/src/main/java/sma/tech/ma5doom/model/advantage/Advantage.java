package sma.tech.ma5doom.model.advantage;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Advantage {

    @SerializedName("advantages")
    @Expose
    private List<Advantage_> advantages = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("count")
    @Expose
    private Integer count;

    public List<Advantage_> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(List<Advantage_> advantages) {
        this.advantages = advantages;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
