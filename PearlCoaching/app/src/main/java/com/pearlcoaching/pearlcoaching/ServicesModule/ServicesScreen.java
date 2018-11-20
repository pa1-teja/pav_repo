package com.pearlcoaching.pearlcoaching.ServicesModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.pearlcoaching.pearlcoaching.R;
import com.pearlcoaching.pearlcoaching.baseclass.BaseActivity;

public class ServicesScreen extends BaseActivity implements OnServiceInteraction {

    private static final int REQUEST_PHONE_CALL = 9;
    private Bundle bundle;
    FloatingActionButton extra_stuff;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private boolean isFABOpen;
    private Intent phoneIntent;
    private FloatingActionButton fab;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        bundle = getIntent().getExtras();
        addFragment(ServiceHome.newInstance("",""),false,true,ServiceHome.class.getName());
//        extra_stuff = findViewById(R.id.extra_stuff);

         fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab1.setVisibility(View.GONE);
        fab2.setVisibility(View.GONE);
//        fab3 = findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    fab.animate().rotation(90);
                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                    showFABMenu();
                }else{
                    fab.animate().rotation(-90);
                    closeFABMenu();
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.web_link_url)));
                startActivity(browserIntent);
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 phoneIntent = new Intent(Intent.ACTION_CALL);
                 phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                phoneIntent.setData(Uri.parse("tel:" + getString(R.string.phone)));
                if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(ServicesScreen.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ServicesScreen.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        getApplicationContext().startActivity(phoneIntent);
                    }
                } else {
                    getApplicationContext().startActivity(phoneIntent);
                }
            }
        });
    }

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationX(-getResources().getDimension(R.dimen.standard_55));
//        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationX(0);
//        fab3.animate().translationY(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_PHONE_CALL:
                getApplicationContext().startActivity(phoneIntent);

        }
    }

    @Override
    public void goToHome(int serviceId) {

    }

    @Override
    public void onServiceInfo(int serviceId) {
//        Toast.makeText(this, "onServiceBooking: "+serviceId,Toast.LENGTH_LONG).show();
        addFragment(ServiceInfo.newInstance(serviceId),true,true,ServiceInfo.class.getName());
    }

    @Override
    public void onServiceBooking(int serviceId) {
        bundle.putInt(ServiceBooking.ARG_PARAM1, serviceId);
        addFragment(ServiceBooking.newInstance(serviceId), true,false,ServiceBooking.class.getName(),bundle);
    }

    @Override
    public void onThankYou(String text) {
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //this will clear the back stack and displays no animation on the screen
        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        addFragment(ThankYou.newInstance("",""),true,false,ThankYou.class.getName());
    }
}
