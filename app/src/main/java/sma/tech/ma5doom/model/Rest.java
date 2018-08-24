package sma.tech.ma5doom.model;

public class Rest {

    String imageUrl ;
    String name;

    public Rest(String name){
        this.name=name;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
