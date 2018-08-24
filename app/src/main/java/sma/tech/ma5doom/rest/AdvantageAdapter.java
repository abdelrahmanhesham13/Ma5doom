package sma.tech.ma5doom.rest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.advantage.Advantage_;

public class AdvantageAdapter extends RecyclerView.Adapter<AdvantageAdapter.MrafkViewHolder> {


    private Context mContext;
    private ArrayList<Advantage_> data;
    public boolean checkedAdvantages[];


    public AdvantageAdapter(Context context , ArrayList<Advantage_> data ) {
        mContext = context;
        this.data=data;
        checkedAdvantages=new boolean[100];

    }

    @NonNull
    @Override
    public AdvantageAdapter.MrafkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewAdvantage_) {
        return new AdvantageAdapter.MrafkViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdvantageAdapter.MrafkViewHolder holder, final int position) {
        if (data==null || data.size()==0)
            return;


        Advantage_ type = data.get(position);

        holder.name.setText(type.getName());
        if (checkedAdvantages[Integer.parseInt(data.get(position).getId())]==true){
            holder.checkBox.setChecked(true);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        checkedAdvantages[Integer.parseInt(data.get(position).getId())]=true;
                    else
                        checkedAdvantages[Integer.parseInt(data.get(position).getId())]=false;

                }catch (Exception e){

                }
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (data!=null)
            return data.size();

        return 0;
    }

    public void changeData(ArrayList<Advantage_> newData) {
        data=newData;
        notifyDataSetChanged();
    }


    static class MrafkViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CheckBox checkBox;

        public MrafkViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_rest_mrafk, parent, false));
            name = (TextView) itemView.findViewById(R.id.tv_name);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_mark);


        }

    }

}
