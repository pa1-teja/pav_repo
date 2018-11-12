package com.pearlcoaching.pearlcoaching.baseclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.pearlcoaching.pearlcoaching.R;

public class BaseActivity extends AppCompatActivity {

    protected void addFragment(Fragment fragment, boolean isReplace, boolean isAddToBackStack, String fragment_name, Bundle data) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(data);
        if(isReplace) {
            fragmentTransaction.replace(R.id.base_container, fragment, fragment_name);
        } else {
            fragmentTransaction.add(R.id.base_container, fragment, fragment_name);
        }
        if(isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragment_name);
        }
        fragmentTransaction.commit();
    }
    protected void addFragment(Fragment fragment, boolean isReplace, boolean isAddToBackStack, String fragment_name) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(isReplace) {
            fragmentTransaction.replace(R.id.base_container, fragment, fragment_name);
        } else {
            fragmentTransaction.add(R.id.base_container, fragment, fragment_name);
        }
        if(isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragment_name);
        }
        fragmentTransaction.commit();
    }
}
