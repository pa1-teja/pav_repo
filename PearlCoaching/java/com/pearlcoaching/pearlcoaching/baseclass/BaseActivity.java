package com.pearlcoaching.pearlcoaching.baseclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.pearlcoaching.pearlcoaching.R;

import java.util.zip.Inflater;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
