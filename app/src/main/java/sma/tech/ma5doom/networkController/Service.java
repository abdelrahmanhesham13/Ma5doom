package sma.tech.ma5doom.networkController;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
//
//import tech.android.volley.Request;
//import tech.android.volley.Response;
//import tech.android.volley.VolleyError;
//import tech.android.volley.request.SimpleMultiPartRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

//import sma.sma.tech.task.img.UploadImage;
import sma.tech.ma5doom.img.ImagePojo;
import sma.tech.ma5doom.networkController.model.login.User;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mohanad on 31/07/17.
 */

public class Service {


    /** the Back end  API url*/
    private static String BackEND_API_URL="http://www.mazad-sa.net/ma5dom/api/";
    /**the login back end url */
    private static String LOGIN_WEB_PAGE="login?";

    private static String GET_PRODUCTS_WEB_PAGE="get_products";


    /**the login back end url */
    private static String REGISTER_WEB_PAGE="signup?";

    /**the login back end url */
    private static String GETLOCATIONS_WEB_PAGE="getlocations.php";

    /**the registeration back end url */
    private static String REGISTER_WEB_PAGEL="registration.php";

    /** the request page*/
    private static String REQUEST_RIDE_WEB_PAGE="requesttaxi.php";

    private static String RIDES_WEB_PAGE="user-rides-list.php";

    /**
     * login to the site
     * @param context : the application conext
     * @param email : the user email
     * @param password : the user password
     * @param category : the user category (client , driver)
     * @param callback : the interface callback to do something
     */
    public static void login(Context context ,String email , String password , String category , RequestCallBack callback){
        //define hashMap with query parameters
        HashMap<String , String> params=new HashMap<>();
        params.put("email",email);
        params.put("password",password);
        params.put("category",category);
        //call the Notifier to do a request with POST Method
        BaseRequest.doPost(BackEND_API_URL+LOGIN_WEB_PAGE, context,params,callback);

    }

    /**
     *
     * @param context
     * @param name
     * @param phoneNumber
     * @param email
     * @param password
     * @param category
     * @param callback
     */
    public static void register(Context context ,String name , String phoneNumber , String email , String password, String category , RequestCallBack callback){
        //define hashMap with query parameters
        HashMap<String , String> params=new HashMap<>();
        params.put("name",name);
        params.put("number",phoneNumber);
        params.put("email",email);
        params.put("password",password);
        params.put("category",category);
        //call the Notifier to do a request with POST Method
        BaseRequest.doPost(BackEND_API_URL+REGISTER_WEB_PAGEL, context,params,callback);

    }


    public static void requestRide(Context context , String driver_id
            , String driver_name , String driver_email , String name
            , String phone , String location , String droplocation ,
                                   String latitude, String longitude , String from_latitude , String to_latitude ,
                                   String from_longitude , String to_longitude , RequestCallBack callback){

        HashMap<String , String> params=new HashMap<>();
        params.put("name",name);
        params.put("driver_id",driver_id);
        params.put("driver_email",driver_email);
        params.put("driver_name",driver_name);
        params.put("phone",phone);
        params.put("location",location);
        params.put("droplocation",droplocation);
        params.put("latitude",latitude);
        params.put("longitude",longitude);
        params.put("from_latitude",from_latitude);
        params.put("to_latitude",to_latitude);
        params.put("from_longitude",from_longitude);
        params.put("to_longitude",to_longitude);
        //call the Notifier to do a request with POST Method
        BaseRequest.doPost(BackEND_API_URL+REQUEST_RIDE_WEB_PAGE, context,params,callback);

    }


    public static void getRides(Context context , RequestCallBack callBack){

        HashMap<String , String> params=new HashMap<>();
        params.put("token","");
        BaseRequest.doPost(BackEND_API_URL+RIDES_WEB_PAGE , context ,params ,callBack);
    }

    public static void loginAdmin(String url ,  Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+LOGIN_WEB_PAGE+url , context  ,callBack);

    }
    public static void login(String mobile , String password , Context context , RequestCallBack callBack){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        BaseRequest.doGet(BackEND_API_URL+LOGIN_WEB_PAGE+"mobile="+mobile+"&"+"password="+password+"&token="+preferences.getString("token","null"), context  ,callBack);
    }

    static String MOBILE="mobile=";
    static String PASSWORD="password=";
    static String NAME="name=";
    static String CITY_ID="city_id=";
    static String EMAIL="email=";


    public static void register(String name , String city_id , String email , String mobile
            , String password , String role, Context context , RequestCallBack callBack){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        BaseRequest.doGet(BackEND_API_URL+ REGISTER_WEB_PAGE+"role="+role+"&"+MOBILE+mobile+"&"+PASSWORD+password+"&"+NAME+name+"&"+CITY_ID+city_id
                +"&"+EMAIL+email+"&token="+preferences.getString("token","null"), context  ,callBack);
    }


    static String EDIT_WEBPAGE="edit?";

    public static void edit(String id , String name , String city_id , String email , String mobile
            , String password ,  Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+ EDIT_WEBPAGE+MOBILE+mobile+"&"+PASSWORD+password+"&"+NAME+name+"&"+CITY_ID+city_id
                +"&"+EMAIL+email+"&id="+id, context  ,callBack);
    }



    public static void getProducts(Context context , String subId , RequestCallBack callBack){
        String path=GET_PRODUCTS_WEB_PAGE;
        if (subId.length()>0)
            path+="?subcategory_id="+subId;
        BaseRequest.doGet(BackEND_API_URL+path, context  ,callBack);
    }

    public static void getProductsCategory(Context context , String subId , RequestCallBack callBack){
        String path=GET_PRODUCTS_WEB_PAGE;
        if (subId.length()>0)
            path+="?category_id="+subId;
        BaseRequest.doGet(BackEND_API_URL+path, context  ,callBack);
    }

    public static void getContact(Context context,RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+"get_contact",context,callBack);
    }

    public static void getProducts(String user_id, Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+GET_PRODUCTS_WEB_PAGE+"?user_id="+user_id, context  ,callBack);
    }

    static String PRODUCT_DETAIL_WEB_PAGE="get_product?id=";
    public static void getProductDetails( String product_id , Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+PRODUCT_DETAIL_WEB_PAGE+product_id, context  ,callBack);
    }



    static String RESERVATION="get_reservations?";
    public static void getAllReservations( Context context ,String type , RequestCallBack callBack){
        // Start Main Screen

        User user = new SharedPrefManager(context).getUserDate();
        String id="1";
        Log.i("User_id : ",id);
        if (user!=null){

          id = user.getUser().getId();
            Log.i("User_id : ",id);
        }
        String pls=RESERVATION;
        if (user.getUser().getRole().equals("1")){
            pls="owner_reservations?";
//            url="http://www.mazad-sa.net/ma5dom/api/owner_reservations?user_id=";
        }
        BaseRequest.doGet(BackEND_API_URL+pls+"user_id="+id+"&type="+type, context  ,callBack);
    }


    static String ADDRATE="add_review?user_id=";
    public static void addRate( Context context , String product_id, String comment , String review , RequestCallBack callBack){
        // Start Main Screen
        User user = new SharedPrefManager(context).getUserDate();
        String id="1";
        if (user!=null){
            id = user.getUser().getId();
        }
        else
            return;
        BaseRequest.doGet(BackEND_API_URL+ADDRATE+id+"&product_id="+product_id+"&comment="+comment
                +"&review="+review, context  ,callBack);
    }

    static  String RESERVE_REST_WEBPAGE="reservation?";
    public static void reserveRest( Context context , String product_id, String user_id , String select_date , String select_Time , String duration , RequestCallBack callBack){
        // Start Main Screen
        User user = new SharedPrefManager(context).getUserDate();
        String id="1";
        if (user!=null){
            id = user.getUser().getId();
        }
        else
            return;
        BaseRequest.doGet(BackEND_API_URL+RESERVE_REST_WEBPAGE+"product_id="
                +product_id+"&user_id="+user_id
                +"&select_date="+select_date+"&select_time="
                +select_Time +"&duration="+duration, context  ,callBack);
    }




    static  String FOREGET_PASS_WEBPAGE="forget_password?email=";
    public static void foregetPassword( Context context , String email, RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+FOREGET_PASS_WEBPAGE+email,context  ,callBack);
    }



    static  String FEEDBACK_WEBPAGE="send_feedback?name=";
    public static void sendFeedBack( Context context ,String name , String email ,
                                     String title , String comment , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+FEEDBACK_WEBPAGE+name+
                "&email="+email+"&title="+title+"&comment="+comment,context  ,callBack);
    }


    static String BANKS_WEBPAGE="get_banks";
    public static void getBanks( Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+BANKS_WEBPAGE,context  ,callBack);
    }


    static String UPLOAD_IMAGE_WEBPAGE="upload_image";
    public static void uploadImage(String imagePath , Context context , final RequestCallBack callBack){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(800, TimeUnit.SECONDS)
                .readTimeout(800,TimeUnit.SECONDS).build();

        Retrofit builder = new Retrofit.Builder().baseUrl("http://www.mazad-sa.net").addConverterFactory(GsonConverterFactory.create()).build();

        RetroService api = builder.create(RetroService.class);

//        // Change base URL to your upload server URL.
//        RetroService retroService = new Retrofit.Builder().
//                baseUrl("http://www.mazad-sa.net").
//                client(client).build().
//                create(RetroService.class);

        File file = new File( String.valueOf(Uri.parse(imagePath)));

        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("parameters[0]", file.getName(), reqFile);
        Call<ImagePojo> req = api.postImage(image);

        req.enqueue(new Callback<ImagePojo>() {
            @Override
            public void onResponse(Call<ImagePojo> call, Response<ImagePojo> response) {
                // Do Something
               callBack.success(response.body().getImages().get(0));
               Log.e("respooo",response.body().getImages().get(0));


            }

            @Override
            public void onFailure(Call<ImagePojo> call, Throwable t) {
                Log.d("fdsa",t.toString());
                callBack.error(null);
            }
        });


        //HashMap<String , String> params = new HashMap<>();
//        params.put("parameters[0]",images.get(0));
////        params.put("parameters[1]",images.get(1));
////        params.put("parameters[2]",images.get(2));
////        params.put("parameters[3]",images.get(3));
////        params.put("parameters[4]",images.get(4));
//        BaseRequest.doPost(BackEND_API_URL+UPLOAD_IMAGE_WEBPAGE,context ,params ,callBack);p

//        UploadImage uploadImage= new UploadImage(context);
//        for (int i = 0 ; i<images.size() ; ++i){
//            uploadImage.imageUpload(images.get(0),"http://www.mazad-sa.net/ma5dom/api/upload_image" ,callBack);
//        }
    }


    static String ADD_PRODUCT_WEBPAGE="add_product?";
    public static void addProduc(String user_id , String name , String address ,
                                 String body, String category_id , String subcategory_id
            ,String lat, String lon , ArrayList<String> image,ArrayList<String>prices
            ,String space, ArrayList<Integer> types ,ArrayList<Integer> advantages, Context context , RequestCallBack callBack){

        String pri = "";
        for (int i = 0 ; i<prices.size(); ++i){
            pri+="&prices["+i+"]="+prices.get(i);
        }

        String typeString="";
        for (int i = 0 ; i <types.size() ; ++i){
            typeString+="&type["+i+"]="+types.get(i);
        }

        String advantagesString="";
        for (int i = 0 ; i <advantages.size() ; ++i){
            advantagesString+="&advantage["+i+"]="+advantages.get(i);
        }


       String add=typeString+advantagesString;

        String url = BackEND_API_URL+ADD_PRODUCT_WEBPAGE+"user_id="+user_id;
        url+="&name="+name;
        url+="&address="+address;
        url+="&body="+body;
        url+="&category_id="+category_id;
        url+="&subcategory_id="+subcategory_id;
        url+="&lat="+lat;
        url+="&long="+lon;
        String imageString="";
        for(int i= 0 ; i<image.size() ; ++i){
            imageString+="&images["+i+"]="+image.get(i);
        }
        url+=imageString;
        url+="&space="+space;
        url+=add;
        url+=pri;


        BaseRequest.doGet(url,context  ,callBack);
    }

//    public static void editProduct(String id , String user_id , String name , String address ,
//                                 String body, String category_id , String subcategory_id
//            ,String lat, String lon , String image,ArrayList<String>prices
//            ,String space, ArrayList<Integer> types ,ArrayList<Integer> advantages, Context context , RequestCallBack callBack){
//
//        String pri = "";
//        for (int i = 0 ; i<prices.size(); ++i){
//            pri+="&prices["+i+"]="+prices.get(i);
//        }
//
//        String typeString="";
//        for (int i = 0 ; i <types.size() ; ++i){
//            typeString+="&type["+i+"]="+types.get(i);
//        }
//
//        String advantagesString="";
//        for (int i = 0 ; i <advantages.size() ; ++i){
//            advantagesString+="&advantage["+i+"]="+advantages.get(i);
//        }
//
//
//        String add=typeString+advantagesString;
//
//        String url = BackEND_API_URL+"edit?"+"user_id="+user_id;
//        url+="&name="+name;
//        url+="&id="+id;
//        url+="&address="+address;
//        url+="&body="+body;
//        url+="&category_id="+category_id;
//        url+="&subcategory_id="+subcategory_id;
//        url+="&lat="+lat;
//        url+="&long="+lon;
//        url+="&images[0]="+image;
//        url+="&space="+space;
//        url+=add;
//        url+=pri;
//
//
//        BaseRequest.doGet(url,context  ,callBack);
//    }
//

    static String EDIT_PRODUCT_WEBPAGE="edit_product?";
    public static void editProduc(String id  ,String user_id , String name , String address ,
                                  String body, String category_id , String subcategory_id
            ,String lat, String lon , ArrayList<String> image,ArrayList<String>prices
            ,String space, ArrayList<Integer> types ,ArrayList<Integer> advantages, Context context , RequestCallBack callBack){

        String pri = "";
        for (int i = 0 ; i<prices.size(); ++i){
            pri+="&prices["+i+"]="+prices.get(i);
        }

        String typeString="";
        for (int i = 0 ; i <types.size() ; ++i){
            typeString+="&type["+i+"]="+types.get(i);
        }

        String advantagesString="";
        for (int i = 0 ; i <advantages.size() ; ++i){
            advantagesString+="&advantage["+i+"]="+advantages.get(i);
        }


        String add=typeString+advantagesString;

        String url = BackEND_API_URL+EDIT_PRODUCT_WEBPAGE+"user_id="+user_id;
        url+="&name="+name;
        url+="&address="+address;
        url+="&body="+body;
        url+="&category_id="+category_id;
        url+="&subcategory_id="+subcategory_id;
        url+="&id="+id;
        url+="&lat="+lat;
        url+="&long="+lon;
        String imageString="";
        for(int i= 0 ; i<image.size() ; ++i){
            imageString+="&images["+i+"]="+image.get(i);
        }
        url+=imageString;
        url+="&space="+space;
        url+=add;
        url+=pri;


        BaseRequest.doGet(url,context  ,callBack);

    }



    static String MRAFK_WEBPAGE="get_types";
    public static void getMrafk( Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+MRAFK_WEBPAGE,context  ,callBack);
    }

    static String ADVANTAGES_WEBPAGE="get_advantages";
    public static void getAdvantages( Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+ADVANTAGES_WEBPAGE,context  ,callBack);
    }



    static String CATEGORIES_WEBPAGE="get_category";
    public static void getCategories( Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+CATEGORIES_WEBPAGE,context  ,callBack);
    }

    static String SUB_CATEGORIES_WEBPAGE="get_subcategory";
    public static void getSubCategories( Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+SUB_CATEGORIES_WEBPAGE,context  ,callBack);
    }


    static String ADD_TO_FAVORITE_WEBPAGE="add_favourite?";
    public static void addRestToFavorite(String user_id ,String product_id, Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+ADD_TO_FAVORITE_WEBPAGE+"user_id="+user_id+"&product_id="+product_id,context  ,callBack);
    }


    static String GET_FAVORITE_WEBPAGE="my_favourites?";
    public static void getFavoritesRests(String user_id , Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+GET_FAVORITE_WEBPAGE+"user_id="+user_id,context  ,callBack);
    }


    static String DELETE_REST_WEBPAGE="delete_product?";
    public static void deleteRest(String user_id ,String product_id, Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+DELETE_REST_WEBPAGE+"user_id="+user_id+"&id="+product_id,context  ,callBack);
    }


    static String GET_RAting_WEBPAGE="get_reviews?";
    public static void getReviews(String product_id, Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+GET_RAting_WEBPAGE+"id="+product_id,context  ,callBack);
    }


    static String NOTIFICATION_WEBPAGE="get_notifications?";
    public static void getNotifications(String user_id ,Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+NOTIFICATION_WEBPAGE+"user_id="+user_id,context  ,callBack);
    }

    static String DELETE_NOTIFICATION_WEBPAGE="delete_notification?";
    public static void deleteNotification(String id , Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+DELETE_NOTIFICATION_WEBPAGE+"id="+id,context  ,callBack);
    }



    static String CANCEL_RESERVATION_WEBPAGE="cancel_reservation?";
    public static void cancelReservation(String user_id ,String id, Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+CANCEL_RESERVATION_WEBPAGE+"user_id="+user_id+"&id="+id,context  ,callBack);
    }


    static String ACCEPET_RESERVATION_WEBPAGE="accept_reservation?";
    public static void acceptReservation(String user_id ,String id, Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+ACCEPET_RESERVATION_WEBPAGE+"user_id="+user_id+"&id="+id,context  ,callBack);
    }


    static String CITIES_WEBPAGE="cities";
    public static void getCities( Context context , RequestCallBack callBack){
        BaseRequest.doGet(BackEND_API_URL+CITIES_WEBPAGE,context  ,callBack);
    }



}
