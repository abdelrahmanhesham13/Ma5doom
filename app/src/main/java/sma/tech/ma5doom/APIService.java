package sma.tech.ma5doom;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by NaderNabil216@gmail.tech on 5/9/2018.
 */
public interface APIService {

    @POST("upload_image")
    Call<UploadImageResponse> UploadImage(@Body RequestBody requestBody);


}
