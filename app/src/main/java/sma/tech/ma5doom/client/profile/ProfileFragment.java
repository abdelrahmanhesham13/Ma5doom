//package att.com.task.client.profile;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import sma.sma.com.task.R;
//import sma.sma.com.task.home.HomeCallBack;
//import sma.sma.com.task.networkController.RequestCallBack;
//import sma.sma.com.task.networkController.Service;
//import sma.sma.com.task.utils.NetworkUtils;
//
//public class ProfileFragment extends Fragment {
//
//    TextView phone , city , password ;
//    Button editBtn;
//    boolean isSave=false;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.fragment_profile, container, false);
//
//        phone = (TextView)v.findViewById(R.id.et_phone);
//        city = (TextView)v.findViewById(R.id.et_city);
//        password = (TextView)v.findViewById(R.id.et_pass);
//        editBtn = (Button)v.findViewById(R.id.btn_edit);
//        progressDialog=new ProgressDialog(getContext());
//
//        password.setEnabled(false);
//        city.setEnabled(false);
//        phone.setEnabled(false);
//
//        editBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isSave){
//                    edit();
//                }else {
//                    editBtn.setText("حفظ");
//                    editBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
//                    isSave=true;
//                    password.setEnabled(true);
//                    city.setEnabled(true);
//                    phone.setEnabled(true);
//                }
//
//            }
//        });
//        return v;
//
//    }
//
//
//
//    private HomeCallBack callBack;
//
//    public void setCallBack(HomeCallBack callBack){
//        this.callBack=callBack;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        getActivity().getMenuInflater().inflate(R.menu.home, menu);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (item.getItemId()==R.id.action_back || item.getItemId()==R.id.home){
//            callBack.onBack();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    public void edit() {
//        String phoneT , cityT , passwordT;
//
//
//
//        if (!NetworkUtils.isNetworkConnected(getContext())) {
//            Toast.makeText(getContext(), "تاكد من الاتصال بالانترنت اولا واعد المحاولة", Toast.LENGTH_LONG).show();
//            return ;
//        }
//
//        phoneT = phone.getText().toString();
//        cityT=city.getText().toString();
//        passwordT=password.getText().toString();
//
//        if (phoneT == null || phoneT.length() < 4) {
//            phone.setError("ادخل رقم صحيح");
//            phone.requestFocus();
//            return;
//        }
//
//
//        if (passwordT == null || passwordT.length() < 4) {
//            password.setError("ادخل رقم سري اطول من 4 احرف");
//            password.requestFocus();
//            return;
//        }
//
//        try{
//            showLoading();
//            Service.sendFeedBack(getContext(), name, emai, title, subject, new RequestCallBack() {
//                @Override
//                public void success(String response) {
//                    try {
//                        JSONObject root= new JSONObject(response);
//                        boolean status = root.getBoolean("status");
//                        if (status){
//                            showDialog();
//                        }
//                        else {
//                            Toast.makeText(getContext(), "حدث خطأ,تاكد من بياناتك", Toast.LENGTH_LONG).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getContext(), "حدث خطأ,حاول مرة اخري", Toast.LENGTH_LONG).show();
//                        dismissLoading();
//                    }
//                    dismissLoading();
//                }
//
//                @Override
//                public void error(Exception exc) {
//                }
//            });
//
//        }catch (Exception e){
//            dismissLoading();
//            Toast.makeText(getContext(), "حدث خطأ,حاول مرة اخري", Toast.LENGTH_LONG).show();
//        }
//
//        dismissLoading();
//
//    }
//
//
//
//
//    ProgressDialog progressDialog;
//    private void showLoading() {
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();
//
////        progressBar.setVisibility(View.VISIBLE);
//
//    }
//
//    private void dismissLoading() {
//        progressDialog.dismiss();
////        setProgressBarIndeterminateVisibility(false);
//
////        progressBar.setVisibility(View.INVISIBLE);
//
//
//    }
//
//    private void showDialog(){
//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which){
//                    case DialogInterface.BUTTON_POSITIVE:
//                        dialog.dismiss();
//                        callBack.onBack();
//                        break;
//                    default:
//                        dialog.dismiss();
//                        callBack.onBack();
//                        break;
//                }
//            }
//        };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setMessage("تم ارسال الرسالة بنجاح .. شكرا لك").setPositiveButton("موافق", dialogClickListener).show();
//
//    }
//}
