package com.pearlcoaching.pearlcoaching.baseclass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.pearlcoaching.pearlcoaching.R;

public class BaseActivity extends AppCompatActivity {

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
