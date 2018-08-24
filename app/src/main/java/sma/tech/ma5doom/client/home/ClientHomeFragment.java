package sma.tech.ma5doom.client.home;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.home.HomeCallBack;
import sma.tech.ma5doom.model.category.Category;
import sma.tech.ma5doom.model.category.Category_;
import sma.tech.ma5doom.model.category.SubCategory;
import sma.tech.ma5doom.model.category.Subcategory;
import sma.tech.ma5doom.model.products.ClientProducts;
import sma.tech.ma5doom.model.products.Product;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.utils.NetworkUtils;

public class ClientHomeFragment extends Fragment implements Navigator {
    ClientRestAdapter adapter;
    ProgressDialog progressDialog;
    Button categoryBtn, highestRateBtn, closestBtn,subcategoryBtn;


    private ArrayList<Subcategory> subCategoryList;
    private ArrayList<Category_> categoryList;

    boolean sortedByRate = false;

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_client_home, container, false);
        categoryBtn = (Button) v.findViewById(R.id.btn_category);
        highestRateBtn = (Button) v.findViewById(R.id.btn_hidh_rate);
        closestBtn = (Button) v.findViewById(R.id.btn_closest);
        subcategoryBtn = v.findViewById(R.id.btn_subcategory);
//        highSalaryBtn = (Button) v.findViewById(R.id.btn_salary);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        closestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closestBtn.getText().equals("الاقرب")) {
                    sortByDistance(true);
                    closestBtn.setText("الابعد");
                } else {
                    closestBtn.setText("الاقرب");
                    sortByDistance(false);
                }
            }
        });
//
//
        highestRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByRate();
            }
        });

        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByCategory();
            }
        });


        subcategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBySubcategory();
            }
        });
//        categoryBtn.setTextColor(getResources().getColor(R.color.colorPrimary));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            categoryBtn.setCompoundDrawableTintList(this.getResources().getColorStateList(R.drawable.tint_ccolor));
//        }

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recyclerview);
        adapter = new ClientRestAdapter(getContext());
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        progressDialog = new ProgressDialog(getContext());
//        filterBtnClicked(categoryBtn);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData("");
        loadCatAndSub();
        loadCat();

        return v;

    }

    private HomeCallBack callBack;

    public void setCallBack(HomeCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.home, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_back || item.getItemId() == R.id.home) {
            callBack.onBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    ArrayList<Product> productlist;

    private void updateData(ArrayList<Product> productlist) {
        adapter.updateData(productlist);
    }

    private void loadData(String subCatId) {


        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) getActivity());
            return;
        }

        showLoading();

        try {

            Service.getProducts(getContext(),subCatId, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try {
                        Gson gson = new Gson();
                        ClientProducts clientProducts = gson.fromJson(response, ClientProducts.class);


                        if (clientProducts.getStatus()) {
                            Log.i("status", clientProducts.getStatus().toString());
                            productlist = (ArrayList<Product>) clientProducts.getProducts();
                            computeDistances();
                            updateData(productlist);
                        } else {
                            GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",(AppCompatActivity)getContext());

                        }

                        dismissLoading();
                    } catch (Exception e) {
                        Log.e("EX", e.toString());
                        dismissLoading();
                    }

                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });

        } catch (Exception e) {
            GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",(AppCompatActivity)getContext());
        }

    }



    private void loadData(String subCatId,boolean x) {


        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) getActivity());
            return;
        }

        showLoading();

        try {

            Service.getProductsCategory(getContext(),subCatId, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try {
                        Gson gson = new Gson();
                        ClientProducts clientProducts = gson.fromJson(response, ClientProducts.class);


                        if (clientProducts.getStatus()) {
                            Log.i("status", clientProducts.getStatus().toString());
                            productlist = (ArrayList<Product>) clientProducts.getProducts();
                            computeDistances();
                            updateData(productlist);
                        } else {
                            GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",(AppCompatActivity)getContext());
                        }

                        dismissLoading();
                    } catch (Exception e) {
                        Log.e("EX", e.toString());
                        dismissLoading();
                    }

                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });

        } catch (Exception e) {
            GMethods.showSnackBarMessage("حدث خطأ حاول مرة اخري",(AppCompatActivity)getContext());

        }

    }

    private void showLoading() {
//        progressDialog.setMessage("جاري التحميل");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setProgress(0);
//        progressDialog.setMax(100);
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissLoading() {
//        progressDialog.dismiss();
        progressBar.setVisibility(View.INVISIBLE);

    }


    public void filterBtnClicked(Button clickedBtn) {
        clickedBtn.setTextColor(getResources().getColor(R.color.colorBtnBack));

    }


    public void computeDistances() {

        for (Product producte : productlist) {

            double lat2 = 0, lon2 = 0;
            float dis = -1;
            try {
                lat2 = Double.parseDouble(producte.getLat());
                lon2 = Double.parseDouble(producte.getLong());
                if (getDistance(lat2,lon2) == -1) {
                    dis = -1;
                } else {
                    dis = (getDistance(lat2, lon2) / 1000);
                }

                producte.setDistance(dis);
            } catch (Exception e) {
                producte.setDistance(-1);
            }

        }
    }


    Location location;
    public void setMyLocation(Location location){
        this.location=location;
    }

    private float getDistance(double lat2, double lon2) {
        double lon1 = 0, lat1 = 0;
        try {
            lat1 = location.getLatitude();
            lon1 = location.getLongitude();

            if (Double.compare(lat1,0) == 0 && Double.compare(lon1,0) == 0){
                return -1;
            }


            Location loc1 = new Location("");
            loc1.setLatitude(lat1);
            loc1.setLongitude(lon1);

            Location loc2 = new Location("");
            loc2.setLatitude(lat2);
            loc2.setLongitude(lon2);

            float distanceInMeters = loc1.distanceTo(loc2);

            return distanceInMeters;
        } catch (Exception e) {
            //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return -1;
        }

    }


    public void sortByDistance(boolean b){


        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) getActivity());
            return;
        }

        Collections.sort(productlist, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return Float.compare(o1.getDistance(),o2.getDistance());
            }
        });


        if (b) {
            updateData(productlist);
        } else {
            Collections.reverse(productlist);
            updateData(productlist);
        }

    }

    public void sortByCategory(){


        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) getActivity());
            return;
        }

        showSubCategoryDialog();

    }


    public void sortBySubcategory(){


        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) getActivity());
            return;
        }

        showSubCategoryDialog(true);

    }


    public void sortByRate(){


        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) getActivity());
            return;
        }

        Collections.sort(productlist, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return Double.compare(o1.getRate(),o2.getRate());
            }
        });

        if (!sortedByRate){
            Collections.reverse(productlist);
            sortedByRate = true;
        } else {
            sortedByRate = false;
        }

        updateData(productlist);

    }


    private void loadCatAndSub(){
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) getActivity());
            return ;
        }

        try {
            showLoading();
            Service.getSubCategories(getContext(), new RequestCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    SubCategory subCategory = gson.fromJson(response , SubCategory.class);
                    if (subCategory.getStatus()){
                        dismissLoading();
                        subCategoryList=(ArrayList<Subcategory>) subCategory.getSubcategories();

                    }
                }

                @Override
                public void error(Exception exc) {

                }
            });
        }catch (Exception e){

        }

    }


    private void loadCat(){
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) getActivity());
            return ;
        }

        try {
            showLoading();
            Service.getCategories(getContext(), new RequestCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    Category subCategory = gson.fromJson(response , Category.class);
                    Log.i("Status",response);
                    if (subCategory.getStatus()){
                        dismissLoading();
                        categoryList=(ArrayList<Category_>) subCategory.getCategories();
                        Log.i("Status",categoryList.size()+"");
                    }
                }

                @Override
                public void error(Exception exc) {

                }
            });
        }catch (Exception e){

        }

    }

    SubCategoryDialog newFragment;
    public void showSubCategoryDialog() {

        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();

        android.app.Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        newFragment = new SubCategoryDialog();
        newFragment.setData(this , subCategoryList);
//        newFragment.updateData(this,subCategoryList);

        newFragment.show(getActivity().getSupportFragmentManager(), "");
    }


    SubCategoryDialog newFragment1;
    public void showSubCategoryDialog(boolean x) {

        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();

        android.app.Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        newFragment1 = new SubCategoryDialog();
        newFragment1.setData(this , categoryList,true);
//        newFragment.updateData(this,subCategoryList);

        newFragment1.show(getActivity().getSupportFragmentManager(), "");
    }


    @Override
    public void onClick(String id) {
        String catId = id;
        if (newFragment != null) {
            if (newFragment.isVisible()) {
                loadData(catId);
            } else {
                loadData(catId, true);
            }
        } else {
            loadData(catId, true);
        }
        if (newFragment!= null) {
            newFragment.dismiss();
        }

        if (newFragment1 != null) {
            newFragment1.dismiss();
        }




    }
}
