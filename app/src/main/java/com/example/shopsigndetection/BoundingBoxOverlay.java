package com.example.shopsigndetection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.camera.view.PreviewView;

import java.util.ArrayList;
import java.util.List;

public class BoundingBoxOverlay extends View {
    private final Paint paint;
    private List<Rect> boundingBoxes;
    private PreviewView previewView;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;

    public BoundingBoxOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFFFF0000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4.0f);
        boundingBoxes = new ArrayList<>();
    }

    public void setPreviewView(PreviewView previewView) {
        this.previewView = previewView;
    }

    public void setBoundingBoxes(List<Rect> boxes) {
        boundingBoxes = boxes;
        if (previewView != null) {
            calculateScaling();
        }
        invalidate();
    }

    private void calculateScaling() {
        int previewWidth = previewView.getWidth();
        int previewHeight = previewView.getHeight();

        // Get the preview's scale type
        PreviewView.ScaleType scaleType = previewView.getScaleType();

        // Assuming preview is using CENTER_CROP
        if (scaleType == PreviewView.ScaleType.FILL_CENTER) {
            scaleX = (float) getWidth() / previewWidth;
            scaleY = (float) getHeight() / previewHeight;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Rect box : boundingBoxes) {
            @SuppressLint("DrawAllocation") Rect scaledBox = new Rect(
                    (int) (box.left * scaleX),
                    (int) (box.top * scaleY),
                    (int) (box.right * scaleX),
                    (int) (box.bottom * scaleY)
            );
            canvas.drawRect(scaledBox, paint);
        }
    }
}