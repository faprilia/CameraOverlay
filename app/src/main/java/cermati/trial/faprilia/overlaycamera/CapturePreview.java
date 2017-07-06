package cermati.trial.faprilia.overlaycamera;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by faprilia on 7/4/17.
 */

public class CapturePreview extends SurfaceView implements SurfaceHolder.Callback {
    public Bitmap bitmap;
    SurfaceHolder holder;
    Camera camera;
    Context context;
    boolean isPreviewing;
    int[] frameLocation, frameSize;
    Camera.Size camPreviewSize;

    public CapturePreview(Context context) {
        this(context, null);
    }

    public CapturePreview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CapturePreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            releaseCameraAndPreview();
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        if (isPreviewing) {
            camera.stopPreview();
            isPreviewing = false;
        }
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
            camPreviewSize = getOptimalPreviewSize(supportedPreviewSizes, height, width);
            parameters.setPictureSize(camPreviewSize.width, camPreviewSize.height);
            parameters.setPreviewSize(camPreviewSize.width, camPreviewSize.height);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//            parameters.setRotation(getCorrectCameraOrientation(cameraInfo, camera));
            camera.setParameters(parameters);
//            camera.setDisplayOrientation(getCorrectCameraOrientation(cameraInfo, camera));

            camera.startPreview();
            isPreviewing = true;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        isPreviewing = false;
    }

    public Bitmap takeAPicture(final int l, final int t, final int r, final int b) {
        Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                camera.stopPreview();

                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, bitmapOptions);
                bitmap = getCroppedImage(bitmap, l, t, r, b);
                saveImage(bitmap, getFile());
                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
            }
        };

        camera.takePicture(null, null, pictureCallback);

        return bitmap;
    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    public int getCorrectCameraOrientation(Camera.CameraInfo info, Camera camera) {

        int rotation = ((Activity)context).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch(rotation){
            case Surface.ROTATION_0:
                degrees = 270;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 0;
                break;

        }

        int result;
        if(info.facing==Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        }else{
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private File getFile() {
        final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ktp");
        imageRoot.mkdirs();

        return new File(imageRoot, new Date().getTime()+".png");
    }

    public void saveImage(Bitmap imageBmp, File file) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            imageBmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getCroppedImage(Bitmap bitmap, int l, int t, int r, int b) {
        Matrix matrix = new Matrix();
        matrix.postRotate(getCorrectCameraOrientation(new Camera.CameraInfo(), camera));
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int y = l * bitmap.getHeight() / displayMetrics.widthPixels;
        int x = t * bitmap.getWidth() / displayMetrics.heightPixels;
        int h = (r-l) * bitmap.getHeight() / displayMetrics.widthPixels;
        int w = (b-t) * bitmap.getWidth() / displayMetrics.heightPixels;
        Log.d("l.t/x.y/bw.bh/wp.hp", l+"."+t+"."+r+"."+b+"/"+x+"."+y+"/"+bitmap.getWidth()+"."+bitmap.getHeight()+"/"+displayMetrics.widthPixels+"."+displayMetrics.heightPixels);
        return Bitmap.createBitmap(bitmap, x, y, w, h, matrix, true);
    }

    public boolean isPreviewing() {
        return isPreviewing;
    }

    public void resumePreview() {
        camera.startPreview();
        isPreviewing = true;
    }
}
