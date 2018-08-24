package sma.tech.ma5doom.rest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

import sma.tech.ma5doom.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EditRestActivity extends AppCompatActivity {

    Spinner spinnerCategory, spinnerType;

    @BindView(R.id.tv_page_name)
    TextView pageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rest);
        ButterKnife.bind(this);
        pageName.setText("تعديل كافة المعلومات عن الاستراحة");
        android.widget.EditText mEdit = (android.widget.EditText) findViewById(R.id.et_price);
        mEdit.setFocusable(false);
        mEdit.setClickable(true);
        Spinner spinnerCategory = (Spinner) findViewById(R.id.spin_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        android.widget.ArrayAdapter<CharSequence> adapter = android.widget.ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerCategory.setAdapter(adapter);


        Spinner spinnerType = (Spinner) findViewById(R.id.spin_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        android.widget.ArrayAdapter<CharSequence> adapter2 = android.widget.ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerType.setAdapter(adapter2);
    }

    public void showWeekPrices(View view) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        PriceDialgoFragment newFragment = new PriceDialgoFragment();
        newFragment.show(this.getSupportFragmentManager(), "");

    }


    @Override
    protected void onResume() {
        super.onResume();
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().
                updateConfiguration(config, getApplicationContext().
                        getResources().getDisplayMetrics());
    }
}
