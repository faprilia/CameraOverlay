package cermati.trial.faprilia.overlaycamera.fragment;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cermati.trial.faprilia.overlaycamera.view.CapturePreview;
import cermati.trial.faprilia.overlaycamera.R;
import cermati.trial.faprilia.overlaycamera.view.FrameOverlayView;

/**
 * Created by faprilia on 7/4/17.
 */

public class CameraFragment extends  Fragment {

    @BindView(R.id.button_capture) View buttonCapture;
    @BindView(R.id.capture_preview_area) CapturePreview capturePreviewArea;
    @BindView(R.id.captured_image) ImageView capturedImage;
    @BindView(R.id.frame)
    FrameOverlayView frameOverlayView;

    private static CameraFragment instance;

    public static CameraFragment getInstance() {
        if (instance == null) {
            instance = new CameraFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        frameOverlayView.setRect(new RectF(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels));
    }

    @OnClick(R.id.button_capture)
    public void onCaptureClicked(View view) {
        Bitmap captured = capturePreviewArea.takeAPicture
                        (frameOverlayView.getFrameLeft(), frameOverlayView.getFrameTop(),
                            frameOverlayView.getFrameRight(), frameOverlayView.getFrameBottom());
        if (captured != null) {
            capturedImage.setVisibility(View.VISIBLE);
            capturedImage.setImageBitmap(captured);
        }
    }

    public boolean isPreviewing() {
        return capturePreviewArea.isPreviewing();
    }

    public void resumePreview() {
        capturePreviewArea.resumePreview();
    }

}
