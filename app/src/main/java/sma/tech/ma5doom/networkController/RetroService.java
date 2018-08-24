package sma.tech.ma5doom.networkController;

import sma.tech.ma5doom.img.ImagePojo;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetroService {

    @Multipart
    @POST("/ma5dom/api/upload_image")
    Call<ImagePojo> postImage(@Part MultipartBody.Part image);
}
