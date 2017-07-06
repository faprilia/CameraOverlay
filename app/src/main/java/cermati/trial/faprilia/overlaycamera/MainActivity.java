package cermati.trial.faprilia.overlaycamera;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cermati.trial.faprilia.overlaycamera.fragment.Camera2BasicFragment;
import cermati.trial.faprilia.overlaycamera.fragment.CameraFragment;
import cermati.trial.faprilia.overlaycamera.fragment.GalleryFragment;

public class MainActivity extends BaseActivity {
    @BindView(R.id.take_ktp_container)
    FrameLayout takeKTPFrameLayout;

    private GalleryFragment galleryFragment;
    private CameraFragment cameraFragment;
    private Camera2BasicFragment camera2BasicFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar!= null) actionBar.hide();
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        galleryFragment = GalleryFragment.getInstance();
        insertFragment(galleryFragment, R.id.take_ktp_container, GalleryFragment.TAG);
    }

    @OnClick(R.id.button_take_photo)
    public void onButtonCaptureClicked(View view) {
        cameraFragment = CameraFragment.getInstance();
//        camera2BasicFragment = Camera2BasicFragment.newInstance();
        replaceFragment(cameraFragment, R.id.take_ktp_container, Camera2BasicFragment.TAG);
    }

    @OnClick(R.id.button_gallery)
    public void onButtonGalleryClicked(View view) {
        galleryFragment = GalleryFragment.getInstance();
        replaceFragment(galleryFragment, R.id.take_ktp_container, GalleryFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0 || getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            if (!cameraFragment.isPreviewing())
                cameraFragment.resumePreview();
            /*if (camera2BasicFragment.getState() == Camera2BasicFragment.STATE_PICTURE_TAKEN) {
                camera2BasicFragment.returnToPreviewState();
            }*/
        }
    }
}
