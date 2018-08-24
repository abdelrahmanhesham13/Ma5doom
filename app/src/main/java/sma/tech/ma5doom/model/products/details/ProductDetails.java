
package sma.tech.ma5doom.model.products.details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetails {

    @SerializedName("favourite")
    @Expose
    private Boolean favourite;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("type")
    @Expose
    private List<Type> type = null;
    @SerializedName("advantage")
    @Expose
    private List<Advantage> advantage = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("base_url")
    @Expose
    private String baseUrl;
    @SerializedName("product")
    @Expose
    private Product product;

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public List<Type> getType() {
        return type;
    }

    public void setType(List<Type> type) {
        this.type = type;
    }

    public List<Advantage> getAdvantage() {
        return advantage;
    }

    public void setAdvantage(List<Advantage> advantage) {
        this.advantage = advantage;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}

