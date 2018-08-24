package sma.tech.ma5doom.client.rest;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.MainActivity;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.networkController.model.login.User;
import sma.tech.ma5doom.networkController.model.login.User_;
import sma.tech.ma5doom.preferences.SharedPrefManager;
import sma.tech.ma5doom.utils.DataAndTimeClicked;
import sma.tech.ma5doom.utils.DatePickerFragment;
import sma.tech.ma5doom.utils.NetworkUtils;
import sma.tech.ma5doom.utils.TimePickerFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReserveRestActivity extends AppCompatActivity implements DataAndTimeClicked {

    String producy_id;
    @BindView(R.id.et_reserve_date)
    EditText reservDate;

    @BindView(R.id.et_reserve_time)
    EditText reservTime;

    @BindView(R.id.cardView2)
    View mCardView;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    Spinner spinnerDuration;
    private ProgressBar progressBar;
    ProgressDialog progressDialog;

    ArrayAdapter<String> daysAdapter;
    AlertDialog alertDialog;


    SharedPrefManager pf;
    String daysArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_rest);
        producy_id = getIntent().getStringExtra("product_id");
        ButterKnife.bind(this);
        reservDate.setFocusable(false);
        reservDate.setClickable(true);
        progressDialog = new ProgressDialog(this);

        reservTime.setFocusable(false);
        reservTime.setClickable(true);

        daysArray = new String[11];
        daysArray[0] = "اختر عدد ايام الحجز";
        for (int i = 1; i < daysArray.length; ++i)
            daysArray[i] = (i) + "";

        pf = new SharedPrefManager(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(this);
        dismissLoading();


        spinnerDuration = (Spinner) findViewById(R.id.spin_duration);


        daysAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, daysArray) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
//// Create an ArrayAdapter using the string array and a default spinner layout
//        android.widget.ArrayAdapter<CharSequence> adapter = android.widget.ArrayAdapter.createFromResource(this,
//                R.array.reservation_duration_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerDuration.setAdapter(daysAdapter);
        daysAdapter.notifyDataSetChanged();

        if (getSupportActionBar()!=null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.action_bar);
            View appBar = getSupportActionBar().getCustomView();
            TextView title = appBar.findViewById(R.id.title);
            title.setText("حجز الاستراحه");
        }

    }


    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePicker(View view) {
        DialogFragment newFragment = new TimePickerFragment(this);
        newFragment.show(getFragmentManager(), "timePicker");
    }


    @Override
    public void dateSelected(String date) {
        reservDate.setText(date);
    }

    @Override
    public void timeSelected(String time) {
        reservTime.setText(time);
    }

    public void reserve(View view) {

        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة",this);
            return;
        }

        String date = reservDate.getText().toString();
        if (date == null || date.trim().length() < 8) {
            reservDate.setError("اختر تاريخ من فضلك");
            return;
        }

        String time = reservTime.getText().toString();
        if (time == null || time.trim().length() < 3) {
            reservTime.setError("اختر وقت من فضلك");
            return;
        }

        String dur = spinnerDuration.getSelectedItemPosition() + "";

        if (dur.equals("0")) {
            showSnackBarMessage("من فضلك اختار عدد الايام", ReserveRestActivity.this);
            return;
        }

        String user_id = pf.getUserDate().getUser().getId();
        try {
            mBtnRegister.setVisibility(View.GONE);
            mCardView.setVisibility(View.GONE);
            showLoading();
            Service.reserveRest(this, producy_id, user_id, date, time, dur, new RequestCallBack() {
                @Override
                public void success(String response) {
                    mBtnRegister.setVisibility(View.VISIBLE);
                    mCardView.setVisibility(View.VISIBLE);
                    Log.i("Reverse Response : ",response);
                    try {
                        JSONObject root = new JSONObject(response);
                        boolean status = root.getBoolean("status");
                        if (status) {
                            show();
                            mBtnRegister.setVisibility(View.VISIBLE);
                            mCardView.setVisibility(View.VISIBLE);
                        } else {
                            GMethods.showSnackBarMessage("حدث خطأ,تأكد من بياناتك",ReserveRestActivity.this);
                            mBtnRegister.setVisibility(View.VISIBLE);
                            mCardView.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        GMethods.showSnackBarMessage("حدث خطأ,حاول مره اخري",ReserveRestActivity.this);
                        mBtnRegister.setVisibility(View.VISIBLE);
                        mCardView.setVisibility(View.VISIBLE);
                        dismissLoading();
                    }
                    dismissLoading();
                }

                @Override
                public void error(Exception exc) {
                    mBtnRegister.setVisibility(View.VISIBLE);
                    mCardView.setVisibility(View.VISIBLE);
                }
            });

        } catch (Exception e) {
            dismissLoading();
            mBtnRegister.setVisibility(View.VISIBLE);
            mCardView.setVisibility(View.VISIBLE);

            GMethods.showSnackBarMessage("حدث خطأ,حاول مره اخري",ReserveRestActivity.this);
        }

        dismissLoading();

    }


    //    ProgressDialog progressDialog;
    private void showLoading() {

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        mBtnRegister.setVisibility(View.GONE);
        mCardView.setVisibility(View.GONE);
//
//        progressBar.setVisibility(View.VISIBLE);

    }

    private void dismissLoading() {
//        progressBar.setVisibility(View.INVISIBLE);
        progressDialog.dismiss();
//        setProgressBarIndeterminateVisibility(false);

//        progressBar.setVisibility(View.INVISIBLE);


    }

    private void showDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        finish();
                        break;
                    default:
                        finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("تم ارسال طلب حجز الاستراحة بنجاح وبانتظار موافقة صاحب الاستراحة").setPositiveButton("موافق", dialogClickListener).show();

    }


    private void show() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);
        dialogView.findViewById(R.id.btn_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager prefManager = new SharedPrefManager(ReserveRestActivity.this);
                User user = prefManager.getUserDate();

                User_ user_ = user != null ? user.getUser() : null;
                alertDialog.dismiss();
                Intent intent = new Intent(ReserveRestActivity.this, MainActivity.class);
                intent.putExtra("role", user.getUser().getRole());
                intent.putExtra("goToReservations", true);
                startActivity(intent);
                finish();
            }
        });


        alertDialog = dialogBuilder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();

    }


    public static void showSnackBarMessage(String message, AppCompatActivity activity) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }
    }


    public void finishActivity(View v){
        finish();
    }

}
