package sma.tech.ma5doom.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sma.tech.ma5doom.R;

public class RestImageAdapter extends RecyclerView.Adapter<RestImageAdapter.ImageViewHolder> {


    private Context mContext;
    private ArrayList<String> data;
    private ArrayList<Bitmap> dataBtimap;
    boolean isEdit ;
    public  String BASEURL;

    public ArrayList<String>imagePaths;



    ImageCallback callback;

    public RestImageAdapter(Context context , boolean isEdit ) {
        mContext = context;
        this.isEdit=isEdit;

    }

    @NonNull
    @Override
    public RestImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestImageAdapter.ImageViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RestImageAdapter.ImageViewHolder holder, final int position) {

        if (isEdit){
            try {
                Picasso.with(mContext).
                        load(BASEURL+data.get(position)).
                        placeholder(R.drawable.ic_camera).
                        into(holder.restImage);
            }catch (Exception e){

            }

            return;

        }

        if (dataBtimap==null || dataBtimap.size()<=position)
            return;


        else{
            holder.restImage.setImageBitmap(dataBtimap.get(position));

        }


//        try {
//            Picasso.with(mContext).
//                    load(imagesURL+data.get(position)).
//                    placeholder(R.drawable.ic_camera).
//                    into(holder.restImage);
//        }catch (Exception e){
//
//        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callback.imageItemClicked(position);
//            }
//        });



    }

    @Override
    public int getItemCount() {
        if (isEdit){
            if (data!= null)
                return data.size();
            else
                return 0;
        }
        if (dataBtimap!=null)
            return Math.max(dataBtimap.size() , 4);

        return 4;
    }

    public void updateData(ArrayList<String> newData) {
        data=newData;
        notifyDataSetChanged();
    }

    public void appendImageBitmapToList(Bitmap bitmap) {
        if (dataBtimap==null)
            dataBtimap=new ArrayList<>();

        dataBtimap.add(bitmap);

        notifyDataSetChanged();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView restImage;

        public ImageViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_image, parent, false));
            restImage = (ImageView) itemView.findViewById(R.id.rest_image);
        }
    }

    public void setCallback(ImageCallback callback){
        this.callback=callback;
    }

}
