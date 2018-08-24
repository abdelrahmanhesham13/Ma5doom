package sma.tech.ma5doom.client.reservation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class ClientFavoriteReservationFragment extends Fragment {


    ClientFavoritReservationAdapter adapter;
    ProgressBar progressBar;
    SharedPrefManager sp;
    TextView empty_view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.recyclerview, container, false);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recyclerview);
        progressBar= (ProgressBar)v.findViewById(R.id.progressBar);
        empty_view= (TextView)v.findViewById(R.id.empty_view);
        adapter = new ClientFavoritReservationAdapter(getContext());
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        sp= new SharedPrefManager(getContext());
        dismissLoading();
        loadFavorite();

        return v;

    }

    private void loadFavorite(){
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) getActivity());
            return ;
        }

        try {
            showLoading();
            String user_id =sp.getUserDate().getUser().getId();
            Service.getFavoritesRests(user_id, getContext(), new RequestCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    ClientProducts clientProducts = gson.fromJson(response , ClientProducts.class);
                    if (clientProducts.getStatus()){

                        ArrayList<Product> product=(ArrayList<Product>) clientProducts.getProducts();
                        if (product==null || product.size()<1) {
                            empty_view.setVisibility(View.VISIBLE);
                            empty_view.setText("لا يوجد استراحات في المفضله");
                        }
                        else empty_view.setVisibility(View.INVISIBLE);

                        adapter.updateData(product);
                        dismissLoading();
                    }
                    else {
                        dismissLoading();
                        empty_view.setVisibility(View.VISIBLE);
                        empty_view.setText("لا يوجد استراحات في المفضله");
                    }
                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });
        }catch (Exception e){
            dismissLoading();
        }
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

    private void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissLoading(){
        progressBar.setVisibility(View.INVISIBLE);
    }

}
