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
import sma.tech.ma5doom.model.mrafk.Type;

public class MrafkAdapter extends RecyclerView.Adapter<MrafkAdapter.MrafkViewHolder> {


    private Context mContext;
    private ArrayList<Type> data;
    public boolean checkedTypes[];
    boolean isEdit= false;

    public MrafkAdapter(Context context , ArrayList<Type> data ) {
        mContext = context;
        this.data=data;
        checkedTypes=new boolean[100];

    }

    @NonNull
    @Override
    public MrafkAdapter.MrafkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MrafkAdapter.MrafkViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final MrafkAdapter.MrafkViewHolder holder, final int position) {
        if (data==null || data.size()==0)
            return;


        Type type = data.get(position);

        holder.name.setText(type.getName());
        if (checkedTypes[Integer.parseInt(data.get(position).getId())]==true){
            holder.checkBox.setChecked(true);
        }

        holder.name.setText(type.getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        checkedTypes[Integer.parseInt(data.get(position).getId())]=true;
                    else
                        checkedTypes[Integer.parseInt(data.get(position).getId())]=false;

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

    public void changeData(ArrayList<Type> newData) {
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


    public void checkThisValues(){
        isEdit = true;
    }

}
