package sma.tech.ma5doom.client.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.category.Subcategory;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCatViewHolder>  {


    private Context mContext;
    private ArrayList<Subcategory> data;
    Navigator callback;

    public SubCategoryAdapter(Context context) {
        mContext = context;
//        this.data=data;

    }

    public void updateData(ArrayList<Subcategory> data , Navigator callback){
        this.data=data;
        this.callback=callback;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public SubCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubCategoryAdapter.SubCatViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCatViewHolder holder, final int position) {
//        if (data==null || data.size()==0)
        final Subcategory subcategory = data.get(position);
        holder.name.setText(subcategory.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(subcategory.getId());
            }
        });
//

    }

    @Override
    public int getItemCount() {
        if (data!=null)
            return data.size();

        return 0;
    }



    static class SubCatViewHolder extends RecyclerView.ViewHolder {
        public TextView name;


        public SubCatViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_city, parent, false));
            name = (TextView) itemView.findViewById(R.id.city_name);


        }

    }

}
