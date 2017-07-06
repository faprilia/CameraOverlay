package cermati.trial.faprilia.overlaycamera.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cermati.trial.faprilia.overlaycamera.R;

/**
 * Created by faprilia on 7/4/17.
 */

public class GalleryFragment extends Fragment {

    public static String TAG = "gallery_fragment";
    private static GalleryFragment instance;

    public static GalleryFragment getInstance() {
        if (instance == null) {
            instance = new GalleryFragment();
        }
        return instance;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        return view;
    }
}
