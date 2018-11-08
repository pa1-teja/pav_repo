package com.pearlcoaching.pearlcoaching;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ServicesScreen extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private ServiceAdapter mAdapter;
    Bundle data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_screen);

        data = getIntent().getExtras();
         recyclerView = findViewById(R.id.services_list);
//        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
         mLayoutManager = new LinearLayoutManager(ServicesScreen.this);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ServiceAdapter(ServicesScreen.this,data);
        recyclerView.setAdapter(mAdapter);


    }
}
