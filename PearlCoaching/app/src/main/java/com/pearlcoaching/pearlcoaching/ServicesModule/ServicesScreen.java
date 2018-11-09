package com.pearlcoaching.pearlcoaching.ServicesModule;

import android.os.Bundle;
import android.widget.Toast;

import com.pearlcoaching.pearlcoaching.R;
import com.pearlcoaching.pearlcoaching.baseclass.BaseActivity;

public class ServicesScreen extends BaseActivity implements OnServiceInteraction {

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        bundle = getIntent().getExtras();
        addFragment(ServiceHome.newInstance("",""),false,false,ServiceHome.class.getName(),bundle);


    }

    @Override
    public void goToHome(int serviceId) {

    }

    @Override
    public void onServiceInfo(int serviceId) {
//        Toast.makeText(this, "onServiceBooking: "+serviceId,Toast.LENGTH_LONG).show();
        addFragment(ServiceInfo.newInstance(serviceId),true,true,ServiceInfo.class.getName(),bundle);
    }

    @Override
    public void onServiceBooking(int serviceId) {
        addFragment(ServiceBooking.newInstance(serviceId), true,false,ServiceBooking.class.getName(),bundle);
    }
}
