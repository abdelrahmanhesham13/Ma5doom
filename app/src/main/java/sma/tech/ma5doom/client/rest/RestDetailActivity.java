package sma.tech.ma5doom.client.rest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.client.rate.AddRatingFragment;
import sma.tech.ma5doom.client.rate.ShowRatingDialog;
import sma.tech.ma5doom.model.products.details.Advantage;
import sma.tech.ma5doom.model.products.details.Product;
import sma.tech.ma5doom.model.products.details.ProductDetails;
import sma.tech.ma5doom.model.products.details.Type;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.rest.CarouselViewActivity;
import sma.tech.ma5doom.rest.PriceDialgoFragment;
import sma.tech.ma5doom.utils.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RestDetailActivity extends AppCompatActivity {


//    static int[] covers = {R.drawable.ic_cover_1,R.drawable.ic_cover_2,R.drawable.ic_cover_3
//            ,R.drawable.ic_cover_4};

//    static String[] covers;


    ProgressDialog progressDialog;
    String PRODUCT_ID;

    @BindView(R.id.tv_rest_size)
    TextView restSize;

    @BindView(R.id.tv_rest_category)
    TextView restGategory;

    @BindView(R.id.tv_rest_type)
    TextView restType;

    @BindView(R.id.tv_rest_name)
    TextView restName;

    @BindView(R.id.tv_rest_desc)
    TextView restDesc;

    @BindView(R.id.btn_rate)
    Button rateBtn;


    @BindView(R.id.fav_btn)
    Button favBtn;
    boolean favorite;

    AdvantageAdapter advantageAdapter;
    AdvantageAdapter marafkAdapter;
    SharedPrefManager sp;

    CarouselView carouselView;



    MyPagerAdapter myPagerAdapter;
    String userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_rest_detail);
        ButterKnife.bind(this);
        //PagerContainer container = (PagerContainer) findViewById(R.id.pager_container);
        //ViewPager pager = container.getViewPager();
        myPagerAdapter=new MyPagerAdapter();
        //pager.setAdapter(myPagerAdapter);
        //pager.setClipChildren(false);
        sp =new SharedPrefManager(this);
        userID=sp.getUserDate().getUser().getId();

        progressDialog=new ProgressDialog(this);
        PRODUCT_ID= getIntent().getStringExtra("ID");
        //
        //pager.setOffscreenPageLimit(15);

//        container.setPageItemClickListener(new PageItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//               }
//        });
         /*new CoverFlow.Builder()
                    .with(pager)
                    .scale(0.3f)
                    .pagerMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin))
                    .spaceSize(0f)
                    .build();*/

        marafkAdapter = new AdvantageAdapter(this, null,null,1);
        RecyclerView rvMrafk = (RecyclerView) findViewById(R.id.recyclerview_advantage);
        rvMrafk.setAdapter(marafkAdapter);
        rvMrafk.setNestedScrollingEnabled(false);
        rvMrafk.setHasFixedSize(true);
        rvMrafk.setLayoutManager(new GridLayoutManager(this,3));


        advantageAdapter = new AdvantageAdapter(this, null,null,0);
        RecyclerView rvAdvatage = (RecyclerView) findViewById(R.id.recyclerview);
        rvAdvatage.setAdapter(advantageAdapter);
        rvAdvatage.setNestedScrollingEnabled(false);
        rvAdvatage.setHasFixedSize(true);
        rvAdvatage.setLayoutManager(new GridLayoutManager(this,3));
        if (getSupportActionBar()!=null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.action_bar);
        }

        loadData();

    }

    public void showWeekPrices(View view) {



        Bundle bundle =new Bundle();
        bundle.putStringArrayList("prices" , prices);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            PriceDialgoFragment newFragment = new PriceDialgoFragment();
            newFragment.setArguments(bundle);

        newFragment.show(this.getSupportFragmentManager(), "");
    }



    public void showAddRatingDialog(View view) {

        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة",RestDetailActivity.this);
            return ;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        AddRatingFragment newFragment = new AddRatingFragment();
        newFragment.setProduct_id(PRODUCT_ID);
        newFragment.show(this.getSupportFragmentManager(), "");
    }


    public void showClientsRatingDialog(View view) {

        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة",RestDetailActivity.this);
            return ;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ShowRatingDialog newFragment = new ShowRatingDialog();
        newFragment.setProduct_id(PRODUCT_ID);

        newFragment.show(this.getSupportFragmentManager(), "");
    }

    public void reserveRest(View view) {
        Intent intent = new Intent(this , ReserveRestActivity.class);
        intent.putExtra("product_id",product_id);
        startActivity(intent);
    }

    public void addToFavorite(View view) {
        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة",RestDetailActivity.this);
            return ;
        }

       showLoading();
        try{
            String user_id = sp.getUserDate().getUser().getId();
            Service.addRestToFavorite(user_id, PRODUCT_ID, this, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try{
                        JSONObject root =new JSONObject(response);
                        String msg= root.getString("message");
                        boolean status = root.getBoolean("status");
                        if (status){

                            if (favorite){
                                favBtn.setText("اضافة للمفضلة");
                                favorite=false;

                            }else {
                                favBtn.setText("حذف من المفضلة");
                                favorite=true;
                            }
                            dismissLoading();
                            showSnackBarMessage(msg,RestDetailActivity.this);
                        }
                        else {
                            dismissLoading();
                            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",RestDetailActivity.this);

                        }
                    } catch (JSONException e) {
                        dismissLoading();
                        GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",RestDetailActivity.this);
                    }
                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });
        }catch (Exception e){
            dismissLoading();
            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",RestDetailActivity.this);
        }
    }

    public void showImages(View view) {
        Log.v("mgeee","clicked");
        int a=0;
    }


    private class MyPagerAdapter extends PagerAdapter {

        ArrayList<String> imagesUrl;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(RestDetailActivity.this).inflate(R.layout.item_cover,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_cover);
//            imageView.setImageDrawable(getResources().getDrawable(covers[position]));
            Picasso.with(getApplicationContext()).
                    load(baseURl+imagesUrl.get(position)).
                    fit().placeholder(R.drawable.logo).
                    into(imageView);
            container.addView(view);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext() , CarouselViewActivity.class);
                    intent.putStringArrayListExtra("images" , imagesUrl);
                    intent.putExtra("url",baseURl);
                    startActivity(intent);

                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return images==null?0:images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        public void updateData(ArrayList<String> images) {
            imagesUrl=images;
            notifyDataSetChanged();
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


    private void loadData(){

        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت واعد المحاوله",RestDetailActivity.this);
            return ;
        }
        showLoading();


        Service.getProductDetails(PRODUCT_ID+"&user_id="+userID , this, new RequestCallBack() {
            @Override
            public void success(String response) {
               try{
                   Gson gson = new Gson();
                   ProductDetails productDetails = gson.fromJson(response, ProductDetails.class);
                   Log.i("status", productDetails.getStatus().toString());
                   if (productDetails.getStatus()){
                       showData(productDetails);
                       advantageAdapter.changeData((ArrayList<Type>) (productDetails.getType()),null );
                       marafkAdapter.changeData(null,(ArrayList<Advantage>) productDetails.getAdvantage());
                   }
               }catch (Exception e){
                   dismissLoading();
               }
                dismissLoading();

            }

            @Override
            public void error(Exception exc) {
                dismissLoading();
            }
        });

    }

    private void showLoading() {
        progressDialog.setMessage("جاري التحميل");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    private void dismissLoading() {
        progressDialog.dismiss();
    }



    String baseURl;
    ArrayList<String> types;
    ArrayList<String> advanteges , images ,prices;
    Product product;
    float lon , lat;

    String name , adreess  , size ,category , subCategory , product_id;
    String detail;
    public void showData(ProductDetails productDetails){
        baseURl = productDetails.getBaseUrl();

        //wating
//         types= (ArrayList<String>) productDetails.getType();
//        advanteges= (ArrayList<String>) productDetails.getAdvantage();
         product = productDetails.getProduct();
        product_id =product.getId();
         name = product.getName();
         detail = product.getBody();
         lon = Float.parseFloat(product.getLatitude());
         lat = Float.parseFloat(product.getLatitude());
         adreess= product.getAddress();
        images= (ArrayList<String>) product.getImage();
        myPagerAdapter.updateData(images);
        prices = (ArrayList<String>) product.getPrices();
         size = product.getSpace();
         subCategory = product.getSubcategory();
         category = product.getCategory();
         if (productDetails.getRate()==0)
             rateBtn.setVisibility(View.GONE);

         favorite=productDetails.getFavourite();

         if (!favorite){
             favBtn.setText("اضافة للمفضلة");
         }else{
             favBtn.setText("حذف من المفضلة");

         }

         restName.setText(name);
         restSize.setText(size+"متر");
         restType.setText(category);
         restGategory.setText(subCategory);
        restDesc.setText(detail);
        ArrayList<String> realImages = new ArrayList<>();
        for (int i = 0;i<images.size();i++){
            realImages.add(baseURl+images.get(i));
        }
        final ImageViewer.Builder builder = new ImageViewer.Builder(RestDetailActivity.this, realImages);

        ImageListener imageListener =new ImageListener() {
            @Override
            public void setImageForPosition(final int position, ImageView imageView) {


                Picasso.with(RestDetailActivity.this).
                        load(baseURl+images.get(position)).placeholder(R.drawable.logo).
                        into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.setStartPosition(position);
                        builder.show();
                    }
                });

            }
        };


        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setImageListener(imageListener);
        carouselView.setPageCount(images.size());





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

}
