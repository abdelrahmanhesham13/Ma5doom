package sma.tech.ma5doom.contact;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sma.tech.ma5doom.GMethods;
import sma.tech.ma5doom.R;
import sma.tech.ma5doom.home.HomeCallBack;
import sma.tech.ma5doom.networkController.RequestCallBack;
import sma.tech.ma5doom.networkController.Service;
import sma.tech.ma5doom.utils.NetworkUtils;

public class ContactUsFragment extends Fragment {

    TextView mName , mEmail , mTitle , mSubject ,phone,mobile;
    Button mSend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contactus, container, false);
        mName = (TextView)v.findViewById(R.id.et_name);


        mEmail = (TextView)v.findViewById(R.id.et_email);
        mTitle = (TextView)v.findViewById(R.id.et_title);
        mSubject = (TextView)v.findViewById(R.id.et_subject);
        mSend = (Button)v.findViewById(R.id.btn_send);
        phone = v.findViewById(R.id.tv_phone_number);
        mobile = v.findViewById(R.id.tv_mobile_number);
        progressDialog=new ProgressDialog(getContext());
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senFeedback();
            }
        });

        Service.getContact(getContext(), new RequestCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("contact");
                    phone.setText(jsonArray.getString(0));
                    mobile.setText(jsonArray.getString(1));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(Exception exc) {

            }
        });

        return v;

    }



    private HomeCallBack callBack;

    public void setCallBack(HomeCallBack callBack){
        this.callBack=callBack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.home, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.action_back || item.getItemId()==R.id.home){
            callBack.onBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void senFeedback() {
        String name , emai , title , subject;
        name = mName.getText().toString();

        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا ثم اعد المحاوله",(AppCompatActivity) getActivity());
            return ;
        }

        if (name == null || name.length() < 2 ) {
            mName.setError("ادخل اسم صحيح");
            return;
        }

        emai = mEmail.getText().toString();
        if (emai == null || emai.length() < 5) {
            mEmail.setError("ادخل بريد الكتروني صحيح");
            return;
        }

        title=mTitle.getText().toString();
        if (title == null || title.length() < 2) {
            mTitle.setError("ادخل عنوان صحيح");
            return;
        }

        subject=mSubject.getText().toString();
        if (subject == null || subject.length() < 10) {
            mSubject.setError("ادخل موضوع  اكثر من 10 حرف");
            return;
        }


        try{
            showLoading();
            Service.sendFeedBack(getContext(), name, emai, title, subject, new RequestCallBack() {
                @Override
                public void success(String response) {
                    try {
                        JSONObject root= new JSONObject(response);
                        boolean status = root.getBoolean("status");
                        if (status){
                            showDialog();
                        }
                        else {
                            GMethods.showSnackBarMessage("حدث خطأ تأكد من بياناتك",(AppCompatActivity) getActivity());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity) getActivity());
                        dismissLoading();
                    }
                    dismissLoading();
                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });

        }catch (Exception e){
            dismissLoading();
            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity) getActivity());
        }

        dismissLoading();

    }




    ProgressDialog progressDialog;
    private void showLoading() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

//        progressBar.setVisibility(View.VISIBLE);

    }

    private void dismissLoading() {
        progressDialog.dismiss();
//        setProgressBarIndeterminateVisibility(false);

//        progressBar.setVisibility(View.INVISIBLE);


    }

    private void showDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        callBack.onBack();
                        break;
                    default:
                        dialog.dismiss();
                        callBack.onBack();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("تم ارسال الرسالة بنجاح .. شكرا لك").setPositiveButton("موافق", dialogClickListener).show();

    }
}
