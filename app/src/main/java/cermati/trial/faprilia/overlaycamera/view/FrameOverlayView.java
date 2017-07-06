package cermati.trial.faprilia.overlaycamera.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.support.v7.widget.AppCompatImageView;

import cermati.trial.faprilia.overlaycamera.R;

/**
 * Created by faprilia on 7/6/17.
 */

public class FrameOverlayView extends AppCompatImageView {
    private RectF rect;
    private float frameWidth, frameHeight;
    private int bgColorRes;
    private int frameLeft, frameRight, frameTop, frameBottom;

    public FrameOverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //In versions > 3.0 need to define layer Type
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FrameOverlayView, 0, 0);
        try {
            frameWidth = ta.getDimension(R.styleable.FrameOverlayView_frameWidth, 200);
            frameHeight = ta.getDimension(R.styleable.FrameOverlayView_frameHeight, 300);
            bgColorRes = ta.getColor(R.styleable.FrameOverlayView_frameColor, ContextCompat.getColor(context, android.R.color.darker_gray));
        } finally {
            ta.recycle();
        }
    }

    public void setRect(RectF rect) {
        this.rect = rect;

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rect != null) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            paint.setColor(bgColorRes);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPaint(paint);

            frameLeft = (int) (rect.centerX() - (frameWidth/2));
            frameRight = (int) (rect.centerX() + (frameWidth/2));
            frameTop = (int) (rect.centerY() -  (frameHeight));
            frameBottom = (int) (rect.centerY());
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawRect(frameLeft, frameTop, frameRight, frameBottom, paint);
        }
    }

    public int getFrameLeft() {
        return frameLeft;
    }

    public int getFrameRight() {
        return frameRight;
    }

    public int getFrameTop() {
        return frameTop;
    }

    public int getFrameBottom() {
        return frameBottom;
    }
}
