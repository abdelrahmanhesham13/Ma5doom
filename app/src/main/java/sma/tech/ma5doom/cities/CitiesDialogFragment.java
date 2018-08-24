package sma.tech.ma5doom.cities;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.cities.City;

public class CitiesDialogFragment extends AppCompatDialogFragment {

    RecyclerView rv;
    CitiesAdapter adapter;
    CityCallBack callBack;
    ArrayList<City> cities;

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public void setCallBack(CityCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.city_recyclerview, container, false);
        adapter = new CitiesAdapter(getContext() , callBack);
        rv= (RecyclerView)v.findViewById(R.id.recyclerview);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.updateData(cities);

        return v;
    }

    private void dismiss_dialog(){
        this.dismiss();
    }




}