package app.dinus.com.loadingdrawable.render;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import app.dinus.com.loadingdrawable.DensityUtil;

import static android.animation.ValueAnimator.INFINITE;
import static android.animation.ValueAnimator.RESTART;

public abstract class LoadingRenderer {
    private static final long ANIMATION_DURATION = 1300;
    private static final float DEFAULT_SIZE = 56.0f;


    private class IValueAnimator implements ValueAnimator.AnimatorUpdateListener {
        private static final float NaN = -1f;
        private float mAnimValue, mLastValue = NaN;

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float temp = (float) animation.getAnimatedValue();

            if (mLastValue == temp) {
                mAnimValue += 1.0f / 250;

            } else {
                mAnimValue = temp;
            }

            computeRender(mAnimValue);

            invalidateSelf();

            mLastValue = temp;
        }

        public void reset() {
            mLastValue = NaN;
        }
    }

    private final IValueAnimator mAnimatorUpdateListener = new IValueAnimator();

    /**
     * Whenever {@link LoadingDrawable} boundary changes mBounds will be updated.
     * More details you can see {@link LoadingDrawable#onBoundsChange(Rect)}
     */
    protected final Rect mBounds = new Rect();

    private Drawable.Callback mCallback;
    private ValueAnimator mRenderAnimator;

    protected long mDuration;

    protected float mWidth;
    protected float mHeight;

    protected float mProgress;
    protected boolean mEndAnimFlag;

    public LoadingRenderer(Context context) {
        initParams(context);
        setupAnimators();
        reset();
    }

    @Deprecated
    protected void draw(Canvas canvas, Rect bounds) {
    }

    protected void draw(Canvas canvas) {
        draw(canvas, mBounds);
    }

    public abstract void computeRender(float renderProgress);

    protected abstract void setAlpha(int alpha);

    protected abstract void setColorFilter(ColorFilter cf);

    protected abstract void reset();

    public void setProgress(float progress) {
        mProgress = progress;
        if (progress >= 0 && !isRunning()) {
            start();
        }

        invalidateSelf();
    }

    protected void addRenderListener(Animator.AnimatorListener animatorListener) {
        if (mRenderAnimator != null) {
            mRenderAnimator.addListener(animatorListener);
        }
    }

    protected void start() {
        reset();

        if (mRenderAnimator != null) {
            mRenderAnimator.addUpdateListener(mAnimatorUpdateListener);

            mRenderAnimator.setRepeatCount(INFINITE);
            mRenderAnimator.setDuration(mDuration);
            mRenderAnimator.start();
        }
    }

    protected void stop() {
        if (mRenderAnimator != null) {
            // if I just call mRenderAnimator.end(),
            // it will always call the method onAnimationUpdate(ValueAnimator animation)
            // why ? if you know why please send email to me (dinus_developer@163.com)
            mRenderAnimator.removeUpdateListener(mAnimatorUpdateListener);

            mRenderAnimator.setRepeatCount(0);
            mRenderAnimator.setDuration(0);
            mRenderAnimator.end();
        }

        mAnimatorUpdateListener.reset();
    }

    public void stop(boolean reset) {
        if (mRenderAnimator != null) {
            // if I just call mRenderAnimator.end(),
            // it will always call the method onAnimationUpdate(ValueAnimator animation)
            // why ? if you know why please send email to me (dinus_developer@163.com)
            mRenderAnimator.removeUpdateListener(mAnimatorUpdateListener);

            mRenderAnimator.setRepeatCount(0);
            mRenderAnimator.setDuration(0);
            mRenderAnimator.end();
        }

        if (reset) {
            mAnimatorUpdateListener.reset();
        }
    }

    public void end() {
        mEndAnimFlag = true;
    }

    protected boolean isRunning() {
        return mRenderAnimator.isRunning();
    }

    void setCallback(Drawable.Callback callback) {
        this.mCallback = callback;
    }

    void setBounds(Rect bounds) {
        mBounds.set(bounds);
    }

    private void initParams(Context context) {
        mWidth = DensityUtil.dip2px(context, DEFAULT_SIZE);
        mHeight = DensityUtil.dip2px(context, DEFAULT_SIZE);

        mDuration = ANIMATION_DURATION;
    }

    private void setupAnimators() {
        mRenderAnimator = ValueAnimator.ofFloat(0, 1);
        mRenderAnimator.setRepeatCount(INFINITE);
        mRenderAnimator.setRepeatMode(RESTART);
        mRenderAnimator.setDuration(mDuration);
        //the default interpolator is AccelerateDecelerateInterpolator
        mRenderAnimator.setInterpolator(new LinearInterpolator());
        mRenderAnimator.addUpdateListener(mAnimatorUpdateListener);
    }

    private void invalidateSelf() {
        if (mCallback != null) {
            mCallback.invalidateDrawable(null);
        }
    }

    public void invalidate() {
        invalidateSelf();
    }
}
