package sma.tech.ma5doom;


import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NaderNabil216@gmail.tech on 5/10/2018.
 */
public class PublishAdsPresenter {
    public void AddImage(File Image, final UploadImageCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("parameters[0]", Image.getName(), RequestBody.create(MediaType.parse("image/*"), Image));
        MultipartBody body = builder.build();
        APIService service = RetrofitClient.getService();
        Call<UploadImageResponse> call = service.UploadImage(body);
        call.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                if (response.code() == 200) {
                    callBack.OnSuccess(response.body().getImagePath());
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }


}
