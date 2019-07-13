package io.github.mikolasan.imperialrussia;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ScaleImageView extends androidx.appcompat.widget.AppCompatImageView {
    private void fixImageView() {
        this.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    public ScaleImageView(Context context) {
        super(context);
        fixImageView();
    }

    public ScaleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        fixImageView();
    }

    public ScaleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        fixImageView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        // this flag determines if should measure height manually dependent of width
        boolean scaleToWidth;
        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            scaleToWidth = true;
        } else if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            scaleToWidth = false;
        } else
            throw new IllegalStateException("width or height needs to be set to match_parent or a specific dimension");

        if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0) {
            // nothing to measure
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            if (scaleToWidth) {
                int iw = this.getDrawable().getIntrinsicWidth();
                int ih = this.getDrawable().getIntrinsicHeight();
                int heightC = width * ih / iw;
                if (height > 0)
                    if (heightC > height) {
                        // dont let hegiht be greater then set max
                        heightC = height;
                        width = heightC * iw / ih;
                    }

                this.setScaleType(ScaleType.CENTER_CROP);
                setMeasuredDimension(width, heightC);

            } else {
                // need to scale to height instead
                int marg = 0;
                if (getParent() != null) {
                    if (getParent().getParent() != null) {
                        marg += ((RelativeLayout) getParent().getParent()).getPaddingTop();
                        marg += ((RelativeLayout) getParent().getParent()).getPaddingBottom();
                    }
                }

                int iw = this.getDrawable().getIntrinsicWidth();
                int ih = this.getDrawable().getIntrinsicHeight();

                width = height * iw / ih;
                height -= marg;
                setMeasuredDimension(width, height);
            }
        }
    }
}
