package com.blankj.dialog.loading;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.ecarx.tip.R;
import com.ecarx.tip.dialog.base.BaseDialog;
import com.ecarx.tip.util.Utils;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/08/09
 *     desc  : 加载框，主动调用dismiss的话至少显示1s
 * </pre>
 *
 * @hide
 */
public class LoadingDialog extends BaseDialog<LoadingDialog.Config> {

    private final Handler mHandler        = new Handler();
    private final int     LEAST_SHOW_TIME = 1000;

    private ImageView           ivLoading;
    private TextView            tvLoadingMessage;
    private LottieAnimationView lavLoading;

    @SuppressLint("ValidFragment")
    private LoadingDialog() {
    }

    /**
     * @hide
     */
    @RestrictTo(LIBRARY)
    public static LoadingDialog newInstance(Config config) {
        Bundle args = new Bundle();
        args.putSerializable(CONFIG_KEY, config);
        LoadingDialog fragment = new LoadingDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int bindLayout() {
        return R.layout.dialog_xc_loading;
    }

    @Override
    protected void initView() {
        lavLoading = findViewById(R.id.lav_loading);
        ivLoading = findViewById(R.id.iv_loading);
        tvLoadingMessage = findViewById(R.id.tv_loading_message);

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                lavLoading.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ivLoading.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    protected void apply(Config config) {
        if (!Utils.isEmpty(config.loadMessage)) {
            tvLoadingMessage.setText(config.loadMessage);
        }
    }


    @Override
    public void dismiss() {
        long interval = System.currentTimeMillis() - startMillis;
        if (interval > LEAST_SHOW_TIME) {
            super.dismiss();
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoadingDialog.super.dismiss();
                }
            }, LEAST_SHOW_TIME - interval);
        }
    }

    public static class Config extends BaseDialog.BaseConfig<Config> {

        CharSequence loadMessage;

        public Config(FragmentActivity activity) {
            super(activity);
        }

        public Config(FragmentActivity activity, @StyleRes int themeId) {
            super(activity, themeId);
        }

        public Config setLoadingMessage(@NonNull CharSequence loadMessage) {
            this.loadMessage = loadMessage;
            return this;
        }

        public Config setLoadingMessage(@StringRes int textId) {
            this.loadMessage = mActivity.getText(textId);
            return this;
        }
    }
}
