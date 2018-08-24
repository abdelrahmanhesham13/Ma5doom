package sma.tech.ma5doom.rest;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import sma.tech.ma5doom.R;

public class PriceDialgoFragment   extends AppCompatDialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    Button agree;
    TextView sat , son , mon ,tue, wed , thu , fri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_price_display_dialog, container, false);

        ArrayList<String> prices =getArguments().getStringArrayList("prices");
        agree = ( Button )v.findViewById(R.id.btn_agree);
        sat=(TextView)v.findViewById(R.id.tv_sat);
        sat.setText(prices.get(6));

        son=(TextView)v.findViewById(R.id.tv_son);
        son.setText(prices.get(0));

        mon=(TextView)v.findViewById(R.id.tv_mon);
        mon.setText(prices.get(1));

        tue=(TextView)v.findViewById(R.id.tv_tue);
        tue.setText(prices.get(2));

        wed=(TextView)v.findViewById(R.id.tv_wed);
        wed.setText(prices.get(3));

        thu=(TextView)v.findViewById(R.id.tv_thu);
        thu.setText(prices.get(4));

        fri=(TextView)v.findViewById(R.id.tv_fri);
        fri.setText(prices.get(5));



        agree.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 dismiss_dialog();
             }
         });

        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Do all the stuff to initialize your custom view

        return v;
    }

    private void dismiss_dialog(){
        this.dismiss();
    }

}