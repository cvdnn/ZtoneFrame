package app.dinus.com.loadingdrawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import app.dinus.com.loadingdrawable.render.LoadingDrawable;
import app.dinus.com.loadingdrawable.render.LoadingRenderer;
import app.dinus.com.loadingdrawable.render.LoadingRendererFactory;

public class LoadingView extends ImageView {
    private static final String TAG = "LoadingView";
    private LoadingDrawable mLoadingDrawable;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        try {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
            int loadingRendererId = ta.getInt(R.styleable.LoadingView_loading_renderer, 0);
            LoadingRenderer loadingRenderer = LoadingRendererFactory.createLoadingRenderer(context, loadingRendererId);
            setLoadingRenderer(loadingRenderer);
            ta.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void setLoadingRenderer(LoadingRenderer loadingRenderer) {
        mLoadingDrawable = new LoadingDrawable(loadingRenderer);
        setImageDrawable(mLoadingDrawable);
    }

    public <L extends LoadingRenderer> L render() {
        return (L) mLoadingDrawable.render();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        final boolean visible = visibility == VISIBLE && getVisibility() == VISIBLE;
        if (visible) {
//            startAnimation();
        } else {
            stopAnimation();
        }
    }

    public void startAnimation() {
        if (mLoadingDrawable != null) {
            mLoadingDrawable.start();
        }
    }

    public void stopAnimation() {
        if (mLoadingDrawable != null) {
            mLoadingDrawable.stop();
        }
    }
}
