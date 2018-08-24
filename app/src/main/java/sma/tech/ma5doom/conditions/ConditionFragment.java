package sma.tech.ma5doom.conditions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import sma.tech.ma5doom.R;
import sma.tech.ma5doom.home.HomeCallBack;
import sma.tech.ma5doom.networkController.Service;

public class ConditionFragment extends Fragment {

    WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_condition, container, false);
        webView = v.findViewById(R.id.webView);
        webView.loadUrl("http://www.mazad-sa.net/ma5dom/api/webview");
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.setTitle("First Fragment");
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
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

}
