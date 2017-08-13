package com.blankj.dialog.base;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.ecarx.tip.R;
import com.ecarx.tip.util.Utils;

import java.io.Serializable;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/08/07
 *     desc  : 基础对话框
 * </pre>
 */
@SuppressWarnings("unchecked")
public abstract class BaseDialog<T extends BaseDialog.BaseConfig> extends DialogFragment {

    public static final String CONFIG_KEY = "config_key";

    protected T mConfig;

    protected Window mWindow;
    protected Dialog mDialog;
    protected View   mContentView;

    protected long startMillis;

    protected abstract int bindLayout();

    protected abstract void initView();

    protected abstract void apply(T config);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 当屏幕旋转时获取mConfig
        if (savedInstanceState != null) {
            mConfig = (T) savedInstanceState.get(CONFIG_KEY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AppCompatDialog dialog = new AppCompatDialog(mConfig.mActivity, mConfig.mThemeId);
        mWindow = dialog.getWindow();
        mDialog = dialog;
        setBaseView();
        initView();
        apply();
        return dialog;
    }

    protected void setBaseView() {
        mDialog.setContentView(mContentView = LayoutInflater.from(mConfig.mActivity).inflate(bindLayout(), null));
    }

    protected void apply() {
        apply(mConfig);
        if (mConfig.widthScale != -1) {
            mWindow.getAttributes().width = (int) (mConfig.widthScale * Utils.getScreenWidth());
        } else if (mConfig.width != -1) {
            mWindow.getAttributes().width = mConfig.width;
        }
        if (mConfig.heightScale != -1) {
            mWindow.getAttributes().height = (int) (mConfig.heightScale * Utils.getScreenHeight());
        } else if (mConfig.height != -1) {
            mWindow.getAttributes().height = mConfig.height;
        }
        if (mConfig.gravity != -1) {
            mWindow.setGravity(mConfig.gravity);
        }
        if (mConfig.alpha != -1) {
            mWindow.getAttributes().alpha = mConfig.alpha;
        }
        if (mConfig.drawable != null) {
            mWindow.setBackgroundDrawable(mConfig.drawable);
        }
        if (mConfig.resId != -1) {
            mWindow.setBackgroundDrawableResource(mConfig.resId);
        }
        if (!mConfig.cancelable) {
            mDialog.setCancelable(false);
        }
        if (!mConfig.cancelOnTouchOutside) {
            mDialog.setCanceledOnTouchOutside(false);
        }
    }

    public void show() {
        if (mConfig == null) {
            Bundle bundle = getArguments();
            mConfig = (T) bundle.getSerializable(CONFIG_KEY);
        }
        super.show(mConfig.mActivity.getSupportFragmentManager(), mConfig.getClass().getName());
        startMillis = System.currentTimeMillis();
    }


    public <V extends View> V findViewById(int viewId) {
        return (V) mContentView.findViewById(viewId);
    }

    public static class BaseConfig<T extends BaseConfig> implements Serializable {

        protected FragmentActivity mActivity;
        int mThemeId;
        float widthScale  = -1;
        float heightScale = -1;
        int   width       = -1;
        int   height      = -1;
        int   gravity     = -1;
        float alpha       = -1;
        Drawable drawable;
        int     resId      = -1;
        boolean cancelable = true;

        boolean cancelOnTouchOutside = true;

        public BaseConfig(FragmentActivity activity) {
            this(activity, R.style.BaseDialogTheme);
        }

        public BaseConfig(FragmentActivity activity, @StyleRes int themeId) {
            this.mActivity = activity;
            this.mThemeId = themeId;
        }

        public T setSize(@FloatRange(from = 0, to = 1, fromInclusive = false) float widthScale,
                         @FloatRange(from = 0, to = 1, fromInclusive = false) float heightScale) {
            this.widthScale = widthScale;
            this.heightScale = heightScale;
            return (T) this;
        }

        public T setWidth(@FloatRange(from = 0, to = 1, fromInclusive = false) float widthScale) {
            this.widthScale = widthScale;
            return (T) this;
        }

        public T setHeight(@FloatRange(from = 0, to = 1, fromInclusive = false) float heightScale) {
            this.heightScale = heightScale;
            return (T) this;
        }

        public T setWidth(@IntRange(from = 8) int width) {
            this.width = width;
            return (T) this;
        }

        public T setHeight(@IntRange(from = 8) int height) {
            this.height = height;
            return (T) this;
        }

        public T setGravity(int gravity) {
            this.gravity = gravity;
            return (T) this;
        }

        public T setAlpha(@FloatRange(from = 0, to = 1, fromInclusive = false) float alpha) {
            this.alpha = alpha;
            return (T) this;
        }

        public T setBg(Drawable drawable) {
            this.drawable = drawable;
            return (T) this;
        }

        public T setBgRes(@DrawableRes int resId) {
            this.resId = resId;
            return (T) this;
        }

        public T setCanceled(boolean cancelable) {
            this.cancelable = cancelable;
            return (T) this;
        }

        public T setCanceledOnTouchOutside(boolean cancel) {
            this.cancelOnTouchOutside = cancel;
            return (T) this;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CONFIG_KEY, mConfig);
    }
}
