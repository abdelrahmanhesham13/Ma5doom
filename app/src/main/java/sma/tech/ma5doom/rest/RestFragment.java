package sma.tech.ma5doom.rest;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;


import java.util.ArrayList;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.home.HomeCallBack;
import sma.tech.ma5doom.model.products.ClientProducts;
import sma.tech.ma5doom.model.products.Product;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.utils.NetworkUtils;

public class RestFragment extends Fragment {
    RestAdapter adapter;
    ProgressBar progressBar;
    SharedPrefManager sp;
    TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.recyclerview, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recyclerview);
        emptyView=(TextView)v.findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);

        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
        sp =new SharedPrefManager(getContext());
        adapter = new RestAdapter(getContext());
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadData();
        return v;

    }


    private void loadData(){
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاوله",(AppCompatActivity)getActivity());
            dismissLoading();
            emptyView.setVisibility(View.VISIBLE);
            return ;
        }


        showLoading();

        try {

            String user_id = sp.getUserDate().getUser().getId();

            Service.getProducts(user_id,getContext(), new RequestCallBack() {
                @Override
                public void success(String response) {
                    try {
                        Gson gson = new Gson();
                        ClientProducts clientProducts = gson.fromJson(response, ClientProducts.class);
                        Log.i("status", clientProducts.getStatus().toString());

                        if (clientProducts.getStatus()){
                            updateData(clientProducts);
                            emptyView.setVisibility(View.INVISIBLE);


                        }else {
                            emptyView.setVisibility(View.VISIBLE);
                            GMethods.showSnackBarMessage("لا يوجد استراحات",(AppCompatActivity)getActivity());


                        }

                        dismissLoading();
                    }catch (Exception e){
                        Log.e("EX" ,e.toString());
                        dismissLoading();
//
}

                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });

        }catch (Exception e){
            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity)getActivity());

        }


    }

    private void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissLoading(){
        progressBar.setVisibility(View.INVISIBLE);
    }


    private HomeCallBack callBack;

    public void setCallBack(HomeCallBack callBack){
        this.callBack=callBack;
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

        if (item.getItemId()==R.id.action_back || item.getItemId()==R.id.home){
            callBack.onBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void updateData(ClientProducts products){
        adapter.updateData((ArrayList<Product>) products.getProducts());
    }

}
