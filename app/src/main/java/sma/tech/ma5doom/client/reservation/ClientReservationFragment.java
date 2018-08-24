package sma.tech.ma5doom.client.reservation;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import java.util.ArrayList;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.home.HomeCallBack;
import sma.tech.ma5doom.model.client.reservation.Product;
import sma.tech.ma5doom.model.client.reservation.Reservation;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.utils.NetworkUtils;

public class ClientReservationFragment extends Fragment {


    Button cur , last;
    ClientReservatinAdapter adapter;
    boolean isCurrent;
    ProgressBar progressBar;
    TextView empty_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_reservation, container, false);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.recyclerview);
        empty_view= (TextView)v.findViewById(R.id.empty_view);
        adapter = new ClientReservatinAdapter(getContext(), null ,true);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        dismissLoading();

        isCurrent=true;
        cur= (Button) v.findViewById(R.id.btn_current_reservation);
        cur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empty_view.setVisibility(View.INVISIBLE);
                changeButtonColor(cur,last);
                isCurrent=true;
                adapter.changeData(null , true);
                loadData();

            }
        });
        last= (Button) v.findViewById(R.id.btn_last_reservation);
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empty_view.setVisibility(View.INVISIBLE);
                changeButtonColor(last,cur);
                isCurrent=false;
                adapter.changeData(null , false);
                loadData();
            }
        });

        changeButtonColor(cur,last);

        loadData();


        return v;

    }

    private void loadData(){
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاوله",(AppCompatActivity) getActivity());
            return ;
        }

        try{
            showLoading();
            Service.getAllReservations(getContext() , isCurrent?"0":"1", new RequestCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    Reservation reservation = gson.fromJson(response , Reservation.class);
                    if (reservation.getStatus()){
                        ArrayList<Product> product=(ArrayList<Product>) reservation.getProducts();
                        if (product==null || product.size()<1){
                            empty_view.setVisibility(View.VISIBLE);
                            if (isCurrent){
                                empty_view.setText("لا يوجد حجوزات حاليه");
                            } else {
                                empty_view.setText("لا يوجد حجوزات سابقه");
                            }
                        }
                        else empty_view.setVisibility(View.INVISIBLE);
                        adapter.changeData(product , isCurrent);
                    }else {
                        empty_view.setVisibility(View.VISIBLE);

                        if (isCurrent){
                            empty_view.setText("لا يوجد حجوزات حاليه");
                        } else {
                            empty_view.setText("لا يوجد حجوزات سابقه");
                        }
                    }
                    dismissLoading();
                }

                @Override
                public void error(Exception exc) {
                }
            });
        }catch (Exception e){
            dismissLoading();
        }

    }

    private void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissLoading(){
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void changeButtonColor(Button curr , Button las){
        curr.setTextColor(Color.parseColor("#FFFFFF"));
        Drawable d = getResources().getDrawable(R.drawable.btn_blue);
        curr.setBackground(d);
        las.setTextColor(Color.parseColor("#000000"));
        Drawable d2 = getResources().getDrawable(R.drawable.btn_white);
        las.setBackground(d2);
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



}
