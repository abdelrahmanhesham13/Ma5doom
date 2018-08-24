package sma.tech.ma5doom.model.cities;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CitiesList {

    @SerializedName("cities")
    @Expose
    private List<City> cities = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}