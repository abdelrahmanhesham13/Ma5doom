package sma.tech.ma5doom;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.tech on 5/15/2018.
 */
public class UploadImageResponse extends SuccessResponse{

    ArrayList<String> images ;

    public String getImagePath() {
        return images.get(0);
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
