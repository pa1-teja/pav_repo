package com.pearlcoaching.pearlcoaching.ServicesModule;

import android.os.Bundle;
import android.widget.Toast;

import com.pearlcoaching.pearlcoaching.R;
import com.pearlcoaching.pearlcoaching.baseclass.BaseActivity;

public class ServicesScreen extends BaseActivity implements OnServiceInteraction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        addFragment(ServiceHome.newInstance("",""),false,false,ServiceHome.class.getName());


    }

    @Override
    public void goToHome(int serviceId) {

    }

    @Override
    public void onServiceInfo(int serviceId) {
        Toast.makeText(this, "onServiceBooking: "+serviceId,Toast.LENGTH_LONG).show();
        addFragment(ServiceInfo.newInstance(serviceId),true,true,ServiceInfo.class.getName());
    }

    @Override
    public void onServiceBooking(int serviceId) {
        addFragment(ServiceBooking.newInstance(serviceId), true,false,ServiceBooking.class.getName());
    }
}
