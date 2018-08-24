package sma.tech.ma5doom.rest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;

public class PriceDialogFragment  extends AppCompatDialogFragment {

    WeekCalBack calBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void setCalBack(WeekCalBack calBack){
        this.calBack=calBack;
    }



    Button agree;
    EditText sat , son , mon ,tue, wed , thu , fri;
    public  boolean isForEdit =false;
    ImageView close;
    ArrayList<String> prices;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_price_dialog, container, false);

        agree = ( Button )v.findViewById(R.id.btn_agree);
        sat=(EditText)v.findViewById(R.id.et_sat);
        son=(EditText)v.findViewById(R.id.et_son);
        mon=(EditText)v.findViewById(R.id.et_mon);
        tue=(EditText)v.findViewById(R.id.et_tue);
        wed=(EditText)v.findViewById(R.id.et_wed);
        thu=(EditText)v.findViewById(R.id.et_thu);
        fri=(EditText)v.findViewById(R.id.et_fri);

        if (isForEdit){
            loadPrices(prices);
        }
        close = v.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

       
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tu =tue.getText().toString();
                String we =wed.getText().toString();
                String th =thu.getText().toString();
                String fr =fri.getText().toString();
                 String sa =sat.getText().toString();
                 String so =son.getText().toString();
                 String mo =mon.getText().toString();


                if (sa.length()<1 || so.length()<1 || mo.length()<1 || tu.length()<1 || we.length()<1 ||
                        th.length()<1 ||fr.length()<1 ){
                    GMethods.showSnackBarMessage("من فضلك ادخل الاسعار كامله",(AppCompatActivity)getActivity());

                }
                else {

                    dismiss_dialog();
                    ArrayList<String>prices=new ArrayList<>();
                    prices.add(so);
                    prices.add(mo);
                    prices.add(tu);
                    prices.add(we);
                    prices.add(th);
                    prices.add(fr);
                    prices.add(sa);

                    calBack.calback(prices);

                }

            }
        });

        // Do all the stuff to initialize your custom view
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return v;
    }

    private void dismiss_dialog(){
        this.dismiss();
    }

    private   void loadPrices(ArrayList<String> prices){
        son.setText(prices.get(0));
        mon.setText(prices.get(1));
        tue.setText(prices.get(2));
        wed.setText(prices.get(3));
        thu.setText(prices.get(4));
        fri.setText(prices.get(5));
        sat.setText(prices.get(6));
    }

    public void setPrices(ArrayList<String> prices){
        isForEdit=true;
        this.prices=prices;
    }

}