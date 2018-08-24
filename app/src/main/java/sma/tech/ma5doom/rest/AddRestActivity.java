package sma.tech.ma5doom.rest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.PublishAdsPresenter;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.UploadImageCallBack;
import sma.tech.ma5doom.img.RealPathUtil;
import sma.tech.ma5doom.maps.MapsActivity;
import sma.tech.ma5doom.model.advantage.Advantage;
import sma.tech.ma5doom.model.advantage.Advantage_;
import sma.tech.ma5doom.model.category.Category;
import sma.tech.ma5doom.model.category.Category_;
import sma.tech.ma5doom.model.category.SubCategory;
import sma.tech.ma5doom.model.category.Subcategory;
import sma.tech.ma5doom.model.mrafk.Mrafk;
import sma.tech.ma5doom.model.mrafk.Type;
import sma.tech.ma5doom.model.products.details.Product;
import sma.tech.ma5doom.model.products.details.ProductDetails;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.utils.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRestActivity extends AppCompatActivity implements View.OnClickListener {


//    @BindView(R.id.im_1)
//    ImageView im1;
//
//    @BindView(R.id.im_2)
//    ImageView im2;
//
//    @BindView(R.id.im_3)
//    ImageView im3;
//
//    @BindView(R.id.im_4)
//    ImageView im4;
//
//    @BindView(R.id.im_5)
//    ImageView im5;

    @BindView(R.id.add_more_photos)
    Button mAddMore;

    @BindView(R.id.et_name)
    EditText restName;

    @BindView(R.id.et_desc)
    EditText restDescription;

    @BindView(R.id.et_location)
    EditText restLocation;


    @BindView(R.id.et_size)
    EditText restSize;

    @BindView(R.id.spin_type)
    Spinner spinnerType;

    @BindView(R.id.spin_category)
    Spinner spinnerCategory;

    @BindView(R.id.btn_add_rest)
    Button addBtn;

    @BindView(R.id.tv_page_name)
    TextView pageName;


    TextView title;

    ArrayList<String>imagesStrings;

    ArrayList<String> weekPrices;
    ArrayList<String> editedImages;

    MrafkAdapter mrafkAdapter;

    private ArrayList<Integer> checkedMarafk;
    private ImageView ic_back, ic_notification, img1, img2, img3, img4;
    private String current_key="0", img1_key = "images[0]", img2_key = "images[1]", img3_key = "images[2]", img4_key = "images[3]";
    private ArrayList<Integer> checkedAdvantage;
    private ArrayList<Category_> categoryList;
    private ArrayList<Subcategory> subCategoryList;

    ArrayList<String> images;

    private HashMap<String, String> SelectedImages = new HashMap<>();


    AdvantageAdapter advantagesAdapter;

    ArrayAdapter<String> categoryAdapter;

    ArrayAdapter<String> subCategoryAdapter;

    RestImageAdapter restImageAdapter;

    LinearLayout imagesParent;

    private static int MAP_REQUEST_CODE=10;
    String lat,lon;

    PublishAdsPresenter presenter;



    int counter = 1;
    SharedPrefManager sp;
    boolean isEdit;
    String product_id;
    private EditText restPrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rest);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        presenter = new PublishAdsPresenter();
        imagesParent = findViewById(R.id.images_parent);
        Intent intent = getIntent();
        images = new ArrayList<>();
        editedImages = new ArrayList<>();
        isEdit = intent.getBooleanExtra("is_edit",false);
        product_id = intent.getStringExtra("product_id");


        restPrices = (android.widget.EditText) findViewById(R.id.et_price);
        restLocation= (android.widget.EditText) findViewById(R.id.et_location);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.action_bar);
            title = getSupportActionBar().getCustomView().findViewById(R.id.title);
            if (title != null) {
                title.setText("اضافة استراحه");
            }
        }


        restPrices.setFocusable(false);
        restPrices.setClickable(true);
        restLocation.setFocusable(false);
        restLocation.setClickable(true);
        ButterKnife.bind(this);
        restName.requestFocus();
        sp = new SharedPrefManager(this);

        imagesStrings = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               addRest();
            }
        });


        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerview);
        mrafkAdapter = new MrafkAdapter(this , null);
        rv.setAdapter(mrafkAdapter);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this,3));

        RecyclerView rvAdavantage= (RecyclerView)findViewById(R.id.recyclerview_advantage);
        advantagesAdapter =  new AdvantageAdapter(this , null);
        rvAdavantage.setAdapter(advantagesAdapter);
        rvAdavantage.setHasFixedSize(true);
        rvAdavantage.setLayoutManager(new GridLayoutManager(this,3));

        //RecyclerView rvImages =  (RecyclerView)findViewById(R.id.recyclerview_images);
        restImageAdapter = new RestImageAdapter(this , isEdit);
        //rvImages.setAdapter(restImageAdapter);
        //rvImages.setHasFixedSize(true);
        //rvImages.setLayoutManager((new GridLayoutManager(this,4)));


        if (isEdit) {
            pageName.setText("تعديل كافة التفاصيل الخاطة بالاستراحة");
            edit(product_id);
            restLocation.setText("تم تحديد الموقع ... اضغط للتعديل");
            restPrices.setText("تم تحديد الاسعار ... اضفط للتعديل");
            addBtn.setText("حفظ");
            title.setText("تعديل الاستراحه");

            //will invoked inside edit so they cant throw exception
//        loadTypes();
//        loadAdvantages();
//        loadCatAndSub();
        }

        else {
            loadTypes();
            loadAdvantages();
            loadCatAndSub();
        }


//
//        im1.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                specialCounter=1;
//                choseImage(null);
//                return true;
//            }
//        });
//
//        im2.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                specialCounter=2;
//                choseImage(null);
//                return true;
//            }
//        });
//
//        im3.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                specialCounter=3;
//                choseImage(null);
//                return true;
//            }
//        });
//
//        im4.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                specialCounter=4;
//                choseImage(null);
//                return true;
//            }
//        });
//
//        im4.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                specialCounter=5;
//                choseImage(null);
//                return true;
//            }
//        });






    }


    private void edit(String product_id){
        loadData(product_id);
    }
    private void loadData(String PRODUCT_ID){

        if (!NetworkUtils.isNetworkConnected(this)) {
            showSnackBarMessage("تأكد من الاتصال بالانترنت ثم اعد المحاوله",AddRestActivity.this);
            return ;
        }
        showLoading();

        Service.getProductDetails(PRODUCT_ID , this, new RequestCallBack() {
            @Override
            public void success(String response) {
                Gson gson = new Gson();
                ProductDetails productDetails = gson.fromJson(response, ProductDetails.class);
                Log.i("status", productDetails.getStatus().toString());
                if (productDetails.getStatus()){
                    showData(productDetails);
//                    advantageAdapter.changeData((ArrayList<sma.sma.tech.task.model.products.details.Type>) (productDetails.getType()),null );
//                    marafkAdapter.changeData(null,(ArrayList<sma.sma.tech.task.model.products.details.Advantage>) productDetails.getAdvantage());
                }
                dismissLoading();

            }

            @Override
            public void error(Exception exc) {
                dismissLoading();
            }
        });

    }


    ArrayList<sma.tech.ma5doom.model.products.details.Advantage>advantages;
    ArrayList<sma.tech.ma5doom.model.products.details.Type> types;
    Product product;
    String cat,sub;
    private void  showData(ProductDetails productDetails){

        advantages=(ArrayList<sma.tech.ma5doom.model.products.details.Advantage>)productDetails.getAdvantage();
        for (int i = 0; i < advantages.size() ; ++i){
           try {
               int id= Integer.parseInt(advantages.get(i).getId());
               advantagesAdapter.checkedAdvantages[id]=true;
           }
           catch (Exception e){

           }
        }

        types = ( ArrayList<sma.tech.ma5doom.model.products.details.Type>)productDetails.getType();

        for (int i = 0; i < types.size() ; ++i){
            try {
                int id= Integer.parseInt(types.get(i).getId());
                mrafkAdapter.checkedTypes[id]=true;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        product= productDetails.getProduct();
        restName.setText(product.getName());
        restSize.setText(product.getSpace());
        restDescription.setText(product.getBody());
        restLocation.setText("تم تحديد الموقع .. اضغط للتعديل");
        lat=product.getLatitude();
        lon=product.getLongitude();

        String baseUrl = productDetails.getBaseUrl();
        imagesUrl =(ArrayList<String> )product.getImage();
        loadImages(baseUrl,imagesUrl);

        weekPrices = ( ArrayList<String>)product.getPrices();
         cat = product.getCategoryId();
         sub = product.getSubcategoryId();

        loadTypes();
        loadAdvantages();
        loadCatAndSub();

    }

    private void loadImages(String url ,ArrayList<String> imgs){

        if (isEdit){
            //restImageAdapter.BASEURL=url;
            //restImageAdapter.updateData(imgs);
            try {
                Picasso.with(this).load(GMethods.IMAGE_URL + imgs.get(0)).fit().into(img1);
                img1_key = imgs.get(0);
                Picasso.with(this).load(GMethods.IMAGE_URL + imgs.get(1)).fit().into(img2);
                img1_key = imgs.get(1);
                Picasso.with(this).load(GMethods.IMAGE_URL + imgs.get(2)).fit().into(img3);
                img1_key = imgs.get(2);
                Picasso.with(this).load(GMethods.IMAGE_URL + imgs.get(3)).fit().into(img4);
                img1_key = imgs.get(3);
            } catch (Exception e){
                e.printStackTrace();
            }
            if (imgs.size() == 4){
                mAddMore.setVisibility(View.VISIBLE);
            }
            if (imgs.size()>4){
                mAddMore.setVisibility(View.VISIBLE);
                for (int i=4;i<imgs.size();i++) {
                    final ImageView circleImageView = new ImageView(this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, 120);
                    lp.setMargins(0, 10, 0, 10);
                    circleImageView.setLayoutParams(lp);
                    Picasso.with(this).load(GMethods.IMAGE_URL + imgs.get(i)).fit().into(circleImageView);
                    imagesParent.addView(circleImageView);
                    circleImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            images.remove(imagesParent.indexOfChild(circleImageView)+3);
                            imagesParent.removeView(circleImageView);
                        }
                    });
                }
            }

            images.addAll(imgs);
        }

//        for (int i = 0 ; i<imgs.size() ; ++i){
//            if (i==0){
//                Picasso.with(getApplicationContext()).
//                        load(url+imgs.get(i)).
//                        placeholder(R.drawable.ic_camera).
//                        into(im1);
//            }
//
//            if (i==1){
//                Picasso.with(getApplicationContext()).
//                        load(url+imgs.get(i)).
//                        into(im2);
//            }
//
//            if (i==2){
//                Picasso.with(getApplicationContext()).
//                        load(url+imgs.get(i)).
//                        into(im3);
//            }
//
//            if (i==3){
//                Picasso.with(getApplicationContext()).
//                        load(url+imgs.get(i)).
//                        into(im4);
//            }
//
//            if (i==4){
//                Picasso.with(getApplicationContext()).
//                        load(url+imgs.get(i)).
//                        into(im5);
//            }
//        }


    }


    private void loadCatAndSub(){
        if (!NetworkUtils.isNetworkConnected(this)) {
            showSnackBarMessage("تأكد من الاتصال بالانترنت ثم اعد المحاوله",AddRestActivity.this);
            return ;
        }

        try {
            showLoading();
            Service.getCategories(this, new RequestCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    Category category = gson.fromJson(response , Category.class);
                    if (category.getStatus()){
                        dismissLoading();
                        categoryList=(ArrayList<Category_>) category.getCategories();
                        showCategory(categoryList);

                    }
                }

                @Override
                public void error(Exception exc) {

                }
            });

            Service.getSubCategories(this, new RequestCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    SubCategory subCategory = gson.fromJson(response , SubCategory.class);
                    if (subCategory.getStatus()){
                        dismissLoading();
                        subCategoryList=(ArrayList<Subcategory>) subCategory.getSubcategories();
                        showSubCategory(subCategoryList);

                    }
                }

                @Override
                public void error(Exception exc) {

                }
            });
        }catch (Exception e){

        }

    }


    private void showCategory(ArrayList<Category_> list){
        String categories[]= new String [list.size()+1];
        categories[0]="فئة الاستراحة";
        for (int i = 1 ; i<categories.length ; ++i){
            categories[i]=list.get(i-1).getName();
        }

        categoryAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,categories){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
//
//        categoryAdapter= new ArrayAdapter<String>
//                (this, android.R.layout.simple_spinner_item,categories); //selected item will look like a spinner set from XML
        categoryAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        if (isEdit){
            int postion=0;
            for (int i = 0 ; i<list.size() ; ++i){
                if (cat.equals(list.get(i).getId())){
                    postion=i+1;
                    Log.i("position",postion+"");
                    spinnerCategory.setSelection(postion);
                    break;
                }
            }

        }

    }

    private void showSubCategory(ArrayList<Subcategory> list){
        String subCategories[]= new String [list.size()+1];
        subCategories[0]="نوع الاستراحة";
        for (int i = 1 ; i<list.size()+1 ; ++i){
            subCategories[i]=list.get(i-1).getName();
        }


//
//        subCategoryAdapter= new ArrayAdapter<String>
//                (this, android.R.layout.simple_spinner_item,subCategories); //selected item will look like a spinner set from XML

        // Initializing an ArrayAdapter
        subCategoryAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,subCategories){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        subCategoryAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerType.setAdapter(subCategoryAdapter);
        subCategoryAdapter.notifyDataSetChanged();

        if (isEdit){
            int postion=0;
            for (int i = 0 ; i<list.size() ; ++i){
                if (sub.equals(list.get(i).getId())){
                    postion=i+1;
                    spinnerType.setSelection(postion);
                    break;
                }
            }
        }

    }

    public void showWeekPrices(View view) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        PriceDialogFragment newFragment = new PriceDialogFragment();
        if (isEdit ||(weekPrices!=null&&weekPrices.size()>4))
            newFragment.setPrices(weekPrices);

        newFragment.setCalBack(new WeekCalBack() {
            @Override
            public void calback(ArrayList<String> prices) {
                weekPrices=prices;
                restPrices.setText("تم تحديد الاسعار ... اضفط للتعديل");

            }
        });

        newFragment.show(this.getSupportFragmentManager(), "");

    }


    private void loadTypes(){
        if (!NetworkUtils.isNetworkConnected(this)) {
            showSnackBarMessage("تأكد من الاتصال بالانترنت ثم اعد المحاوله",AddRestActivity.this);
            return ;
        }

        try{

            showLoading();
            Service.getMrafk(this, new RequestCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    Mrafk mrafk = gson.fromJson(response ,Mrafk.class);
                    if (mrafk.getStatus()){
                       mrafkAdapter.changeData((ArrayList<Type>) mrafk.getTypes());

                    }
                    else{
                        showSnackBarMessage("من فضلك تأكد من بياناتك",AddRestActivity.this);
                        dismissLoading();
                    }

                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });

        }catch (Exception e){
            showSnackBarMessage("حدث خطأ حاول مره اخري",AddRestActivity.this);
            dismissLoading();
        }
    }
    private void loadAdvantages(){
        if (!NetworkUtils.isNetworkConnected(this)) {
            showSnackBarMessage("تأكد من الاتصال بالانترنت ثم اعد المحاوله",AddRestActivity.this);
            dismissLoading();
            return ;
        }

        try{

            Service.getAdvantages(this, new RequestCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    Advantage advantage = gson.fromJson(response , Advantage.class);
                    if (advantage.getStatus()){
                        advantagesAdapter.changeData((ArrayList<Advantage_>) advantage.getAdvantages());
                        dismissLoading();
                    }
                    else{
                        showSnackBarMessage("من فضلك تأكد من بياناتك",AddRestActivity.this);
                    }

                    dismissLoading();

                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });

        }catch (Exception e){
            showSnackBarMessage("حدث خطأ حاول مره اخري",AddRestActivity.this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().
                updateConfiguration(config, getApplicationContext().
                        getResources().getDisplayMetrics());
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5 &&data!=null) {
            new ImageChoser().execute(data);
//            android.net.Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//            if (cursor == null)
//                return;
//
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String filePath = cursor.getString(columnIndex);
            String realPath = null;
            boolean imageSelected=false;

            try {
                if (realPath==null){
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
                    imageSelected=true;
                }

            }catch (Exception e){

            }


//            // SDK < API11
//            if (Build.VERSION.SDK_INT < 11) {
//                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
//                imageSelected=true;
//            }

            try {
                if (realPath==null){
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                    imageSelected=true;
                }

            }catch (Exception e){

            }


            try {
                if (realPath==null){
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                    imageSelected=true;
                }

            }catch (Exception e){

            }
//                // SDK >= 11 && SDK < 19
//            else if (Build.VERSION.SDK_INT < 19)
////
////                // SDK > 19 (Android 4.4)
//            else
//            realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

            if (realPath!=null)
            imagesStrings.add(realPath);
            else {
                showSnackBarMessage("خطأ في اضافة الصوره",AddRestActivity.this);
                return;
            }


            uploadImages(realPath);

        }

        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            Image img = images.get(0);
            try {
                Bitmap bitmapimage = GMethods.GetBitmap(img.getPath(), 100);
                UploadImage(img.getName(), GMethods.GetBitmap(img.getPath(), 700));

                if (current_key.equals(img1_key)) {
                    img1.setImageBitmap(bitmapimage);
                    if (isEdit){
                        this.images.remove(0);
                    }
                } else if (current_key.equals(img2_key)) {
                    img2.setImageBitmap(bitmapimage);
                    if (isEdit){
                        if (images.size()>1) {
                            this.images.remove(1);
                        }
                    }
                } else if (current_key.equals(img3_key)) {
                    img3.setImageBitmap(bitmapimage);
                    if (isEdit){
                        if (images.size()>2) {
                            this.images.remove(2);
                        }
                    }
                } else if (current_key.equals(img4_key)) {
                    img4.setImageBitmap(bitmapimage);
                    mAddMore.setVisibility(View.VISIBLE);
                    if (isEdit){
                        if (images.size()>3) {
                            this.images.remove(3);
                        }
                    }
                } else {
                    ImageView circleImageView = new ImageView(this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, 120);
                    lp.setMargins(0, 10, 0, 10);
                    circleImageView.setLayoutParams(lp);
                    circleImageView.setImageBitmap(bitmapimage);
                    imagesParent.addView(circleImageView);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == MAP_REQUEST_CODE && resultCode==RESULT_OK){
            LatLng newLocation = (LatLng) data.getExtras().get("newlocation");
            String locationName = data.getStringExtra("locationname");
            double latee=newLocation.latitude;
            double lonee=newLocation.longitude;
            restLocation.setText("تم تحديد الموقع .. اضغط للتعديل");
            lat=latee+"";
            lon=lonee+"";

        }
    }

    ArrayList<String>imagesUrl = new ArrayList<>();

    boolean flag=false;
    String name ,size,desc , category_id , typ_id;

    public void addRest() {

//        final ArrayList<String>imagesUrl = new ArrayList<>();

        name = restName.getText().toString();
        desc = restDescription.getText().toString();
        size = restSize.getText().toString();

        if (!isValidData(name,desc,size)){
            dismissLoading();
            return;
        }


//        if (isEdit){
//            addRest();
//            return;
//        }

        if (images.size()==0){
            Snackbar.make(findViewById(R.id.scrollView),"اختر صورة من فضلك", Snackbar.LENGTH_SHORT).show();
            dismissLoading();
            return;
        }




        if (isEdit){



           try{
               String user_id = sp.getUserDate().getUser().getId();
               Service.editProduc(product_id, user_id, name, "address", desc, category_id,
                       typ_id, lat, lon, images, weekPrices, size,checkedMarafk , checkedAdvantage, this, new RequestCallBack() {
                           @Override
                           public void success(String response) {
                              try{
                                  if (response.contains("true")){
                                      showSnackBarMessage("تم تعديل الاستراحه بنجاح",AddRestActivity.this,true);
                                      dismissLoading();
                                      //finish();
                                  }
                                  else {
                                      showSnackBarMessage("خطأ من فضلك اعد المحاوله",AddRestActivity.this);
                                      dismissLoading();
                                  }
                              }catch (Exception e){
                                  showSnackBarMessage("خطأ من فضلك اعد المحاوله",AddRestActivity.this);
                              }
                           }

                           @Override
                           public void error(Exception exc) {
                               dismissLoading();
                           }
                       });
           }catch (Exception e){
               showSnackBarMessage("حدث خطأ حاول مره اخري",AddRestActivity.this);
           }

       }else {
           try{
               String user_id = sp.getUserDate().getUser().getId();
               Service.addProduc(user_id, name, "address", desc, category_id,
                       typ_id, lat, lon, images, weekPrices, size,checkedMarafk , checkedAdvantage, this, new RequestCallBack() {
                           @Override
                           public void success(String response) {
                               showSnackBarMessage("تم اضافة الاستراحة",AddRestActivity.this,true);
                               dismissLoading();
                               //finish();
                           }

                           @Override
                           public void error(Exception exc) {
                               dismissLoading();
                           }
                       });
           }catch (Exception e){
               showSnackBarMessage("خطأ من فضلك اعد المحاوله",AddRestActivity.this);
           }
       }
    }


    ProgressDialog progressDialog;
    private void showLoading() {
        progressDialog.setTitle("جاري التحميل");
        progressDialog.show();
    }

    private void dismissLoading() {
        progressDialog.dismiss();

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void openMap(View view) {
        Intent intent = new Intent(this , MapsActivity.class);
        if (isEdit){
            intent.putExtra("edit",true);
            intent.putExtra("lat",lat);
            intent.putExtra("lon",lon);
        }
        startActivityForResult(intent ,MAP_REQUEST_CODE );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img1:
                PickImage(img1_key);
                break;
            case R.id.img2:
                PickImage(img2_key);
                break;
            case R.id.img3:
                PickImage(img3_key);
                break;
            case R.id.img4:
                PickImage(img4_key);
                break;
        }
    }

    class ImageChoser extends AsyncTask<Intent, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Intent... intents) {
            Bitmap bitmap = null;
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), intents[0].getData());
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            addImage(bitmap);
        }
    }


    private void addImage(Bitmap bitmap){

//        int temp= counter;
//        if (specialCounter!=-10){
//            counter=specialCounter;
////            imagesStrings.remove(specialCounter-1);
//        }
//
//        if (bitmap==null || counter>5)
//            return;
//
//
////        imagesStrings.add(getStringImage(bitmap));
//
//        switch (counter) {
//            case 1:
//                im1.setImageBitmap(bitmap);
//                ++counter;
//                break;
//            case 2:
//                im2.setImageBitmap(bitmap);
//                ++counter;
//                break;
//            case 3:
//                im3.setImageBitmap(bitmap);
//                ++counter;
//                break;
//            case 4:
//                im4.setImageBitmap(bitmap);
//                ++counter;
//                break;
//            case 5:
//                im5.setImageBitmap(bitmap);
//                ++counter;
//                break;
//            default:
//                break;
//        }
//
////        counter=temp;
//        specialCounter=-10;



        restImageAdapter.appendImageBitmapToList(bitmap);
    }


    private void uploadImages(String imagePath){

        if (!NetworkUtils.isNetworkConnected(this)) {
            showSnackBarMessage("تأكد من الاتصال بالانترنت ثم اعد المحاوله",AddRestActivity.this);
            return;
        }

        showLoading();

        try {

             Service.uploadImage(imagePath, this, new RequestCallBack() {
                    @Override
                    public void success(String response) {
                        imagesUrl.add(response);
                        dismissLoading();
                    }

                    @Override
                    public void error(Exception exc) {
                        showSnackBarMessage("خطأ في تحميل الصوره",AddRestActivity.this);
                        dismissLoading();
                    }
                });


        }catch (Exception e){
            Log.e("UploadImage","error uploading image");
            dismissLoading();
        }

    }


    private  boolean isValidData(String name , String desc , String size){
        if (name == null || name.length() < 3) {
            restName.setError("من فضلك ادخل اسم صحيح");
            restName.requestFocus();
            return false;
        }

        if (desc == null || desc.length() < 10) {
            restDescription.setError("من فضلك ادخل وصف صحيح اكثر من 10 احرف");
            return false;
        }


        if (counter==0){
            showSnackBarMessage("من فضلك اختر صوره",AddRestActivity.this);
            return false;
        }


        if (size == null || size.length() <1 ) {
            restSize.setError("من فضلك ادخل مساحة صحيحة");
            return false;
        }

        if (weekPrices==null){
            showSnackBarMessage("من فضلك تأكد من اسعار الاسبوع",AddRestActivity.this);
            return false;
        }

        try{
            category_id = categoryList.get(spinnerCategory.getSelectedItemPosition()-1).getId()+"";
        }catch (Exception e){
            showSnackBarMessage("من فضلك حدد فئه الاستراحه",AddRestActivity.this);

            return  false;
        }


        try{
            typ_id = subCategoryList.get(spinnerType.getSelectedItemPosition()-1).getId()+"";
        }catch (Exception e){
            showSnackBarMessage("من فضلك حدد نوع الاستراحه",AddRestActivity.this);
            return  false;
        }




        checkedMarafk = new ArrayList<>();
        checkedAdvantage = new ArrayList<>();

        for (int i = 0 ; i<mrafkAdapter.checkedTypes.length; ++i){
            if (mrafkAdapter.checkedTypes[i]==true){
                checkedMarafk.add(i);
            }

            if (advantagesAdapter.checkedAdvantages[i]){
                checkedAdvantage.add(i);
            }
        }


        if (checkedMarafk.size()<1){
            showSnackBarMessage("من فضلك اختر بعض المرافق",AddRestActivity.this);
            return false;
        }


        if (checkedAdvantage.size()<1){
            showSnackBarMessage("من فضلك اختر بعض المميزات",AddRestActivity.this);
            return false;
        }


        if (lat==null || lat.length()<1||lon.length()<1){
            restLocation.setError("");
            showSnackBarMessage("من فضلك حدد موقع الاستراحه",AddRestActivity.this);
            return false;
        }

        return  true;

    }


    private void PickImage() {
        current_key = "0";
        ImagePicker.with(this)
                .setFolderMode(true) // folder mode (false by default)
                .setFolderTitle("Select Image") // folder selection title
                .setImageTitle("Select Image") // image selection title
                .setMaxSize(1) //  Max images can be selected
                .setMultipleMode(false) //single mode
                .setShowCamera(true) // show camera or not (true by default)
                .start(); // start image picker activity with Request code
    }



    private void UploadImage(String name, Bitmap bitmap) {
        final ProgressDialog progressDialog = GMethods.show_progress_dialoug(this,
                "جاري رفع الصورة",
                false);
        try {
            final File image = BitmapToFile(name, bitmap);
            presenter.AddImage(image, new UploadImageCallBack() {
                @Override
                public void OnSuccess(String image_path) {
                    SelectedImages.put(current_key, image_path);
                    images.add(image_path);
                    progressDialog.dismiss();
                }

                @Override
                public void OnFailure(String message) {

                }

                @Override
                public void OnServerError() {

                }
            });
        } catch (IOException e) {

        }
    }


    private File BitmapToFile(String name, Bitmap bmap) throws IOException {
        File f = new File(this.getExternalCacheDir().getAbsolutePath() + "/" + name);
        f.createNewFile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f;
    }


    public void choseImage(View view) {
        PickImage();
    }


    private void PickImage(String Key) {
        current_key = Key;
        ImagePicker.with(this)
                .setFolderMode(true) // folder mode (false by default)
                .setFolderTitle("Select Image") // folder selection title
                .setImageTitle("Select Image") // image selection title
                .setMaxSize(1) //  Max images can be selected
                .setMultipleMode(false) //single mode
                .setShowCamera(true) // show camera or not (true by default)
                .start(); // start image picker activity with Request code
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }


    public void finishActivity(View v){
        finish();
    }

    public static void showSnackBarMessage(String message, AppCompatActivity activity) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }
    }


    public void showSnackBarMessage(String message, AppCompatActivity activity,boolean b) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).addCallback(new Snackbar.Callback(){
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    AddRestActivity.this.finish();
                }
            }).show();
        }
    }

}


