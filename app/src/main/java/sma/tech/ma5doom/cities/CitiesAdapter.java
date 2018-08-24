package sma.tech.ma5doom.cities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.cities.City;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> {

    private Context mContext;
    private ArrayList<City>data;
    private CityCallBack callBack;

    public CitiesAdapter(Context context , CityCallBack callBack) {
        this.mContext=context;
        this.callBack=callBack;
    }

    public void updateData(ArrayList<City>data){
        this.data=data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        final City city = data.get(position);
        holder.name.setText(city.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onClick(city);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data==null)
        return 0;

        return data.size();
    }

    class CityViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        public CityViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_city, parent, false));
            name=(TextView)itemView.findViewById(R.id.city_name);


        }


    }
}
