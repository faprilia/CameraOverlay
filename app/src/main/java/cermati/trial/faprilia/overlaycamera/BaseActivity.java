package cermati.trial.faprilia.overlaycamera;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by faprilia on 7/4/17.
 */

public class BaseActivity extends AppCompatActivity {
    protected void insertFragment (Fragment fragment, int fragmentLayoutId, @Nullable String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragmentLayoutId, fragment, tag);
        fragmentTransaction.commit();
    }

    protected void replaceFragment (Fragment fragment, int fragmentLayoutId, @Nullable String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentLayoutId, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    protected void insertFragment (android.app.Fragment fragment, int fragmentLayoutId, @Nullable String tag) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragmentLayoutId, fragment, tag);
        fragmentTransaction.commit();
    }

    protected void replaceFragment (android.app.Fragment fragment, int fragmentLayoutId, @Nullable String tag) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentLayoutId, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
