package sma.tech.ma5doom.client.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sma.tech.ma5doom.CategoryAdapter;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.model.category.Category_;
import sma.tech.ma5doom.model.category.Subcategory;

public class SubCategoryDialog extends AppCompatDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Navigator callback;
    SubCategoryAdapter adapter ;
    CategoryAdapter categoryAdapter;
    ArrayList<Subcategory> data;
    ArrayList<Category_> data2;
    TextView title;
    boolean x;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_subcategory, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        title = v.findViewById(R.id.title);
        adapter =new SubCategoryAdapter(getContext());
        categoryAdapter = new CategoryAdapter(getContext());
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recyclerview);

        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (x){
            loadData(true);
            title.setText("اختر النوع");
            rv.setAdapter(categoryAdapter);
        } else {
            loadData();
            title.setText("اختر الفئه");
            rv.setAdapter(adapter);
        }





        // Do all the stuff to initialize your custom view

        return v;
    }

    public void setData(Navigator callback ,  ArrayList<Subcategory> data) {
        this.callback=callback;
        this.data=data;

    }


    public void setData(Navigator callback , ArrayList<Category_> data2, boolean x) {
        this.callback=callback;
        this.data2=data2;
        this.x = x;

    }

    private void loadData(){
        adapter.updateData(data,callback);
    }


    private void loadData(boolean x){
        categoryAdapter.updateData(data2,callback);
    }







}