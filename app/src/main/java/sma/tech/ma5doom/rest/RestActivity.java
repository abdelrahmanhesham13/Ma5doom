package sma.tech.ma5doom.rest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import sma.tech.ma5doom.R;

public class RestActivity extends AppCompatActivity {

    RestAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rest);

//        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerview);
//        adapter = new RestAdapter(this, FakeData.restfakeData());
//        rv.setAdapter(adapter);
//        rv.setHasFixedSize(true);
//        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
