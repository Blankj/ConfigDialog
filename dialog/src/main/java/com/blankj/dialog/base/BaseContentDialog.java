package com.blankj.dialog.base;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecarx.tip.R;
import com.ecarx.tip.dialog.listener.OnClickListener;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/08/07
 *     desc  : 基础内容对话框
 * </pre>
 */
@SuppressWarnings("unchecked")
public abstract class BaseContentDialog<T extends BaseContentDialog.Config> extends BaseDialog<T> {

    private final byte BTN_LEFT  = -1;
    private final byte BTN_RIGHT = -2;

    private TextView     tvDialogTitle;
    private LinearLayout llDialogBottom;
    private TextView     tvDialogLeftBtn;
    private TextView     tvDialogRightBtn;
    private View         viewBottomDivide;

    @Override
    protected void setBaseView() {
        mDialog.setContentView(mContentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_xc_base_content, null));
        FrameLayout flDialogContent = findViewById(R.id.fl_dialog_content);
        flDialogContent.addView(LayoutInflater.from(getContext()).inflate(bindLayout(), null));

        tvDialogTitle = findViewById(R.id.tv_dialog_title);
        tvDialogLeftBtn = findViewById(R.id.tv_dialog_left_btn);
        tvDialogRightBtn = findViewById(R.id.tv_dialog_right_btn);
        llDialogBottom = findViewById(R.id.ll_dialog_bottom);
        viewBottomDivide = findViewById(R.id.view_bottom_divide);
    }

    @Override
    protected void apply() {
        super.apply();
        if (!TextUtils.isEmpty(mConfig.title)) {
            tvDialogTitle.setVisibility(View.VISIBLE);
            tvDialogTitle.setText(mConfig.title);
        }
        // 如果没设置底部按钮，返回
        if (mConfig.leftListener == null && mConfig.rightListener == null) {
            return;
        }
        llDialogBottom.setVisibility(View.VISIBLE);
        if (mConfig.leftListener != null) {
            setButton(BTN_LEFT, mConfig.leftText, mConfig.leftListener);
        }
        if (mConfig.rightListener != null) {
            setButton(BTN_RIGHT, mConfig.rightText, mConfig.rightListener);
        }
        if (mConfig.leftListener != null && mConfig.leftListener != null) {
            viewBottomDivide.setVisibility(View.VISIBLE);
        }
    }

    private void setButton(final int which, final CharSequence text, final OnClickListener listener) {
        TextView tvWhich;
        switch (which) {
            default:
            case BTN_LEFT:
                tvWhich = tvDialogLeftBtn;
                break;
            case BTN_RIGHT:
                tvWhich = tvDialogRightBtn;
                break;
        }
        tvWhich.setVisibility(View.VISIBLE);
        tvWhich.setText(text);
        tvWhich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(mDialog, v);
            }
        });
    }


    public static class Config<T extends Config> extends BaseConfig<T> {
        CharSequence    title;
        CharSequence    leftText;
        OnClickListener leftListener;
        CharSequence    rightText;
        OnClickListener rightListener;

        public Config(FragmentActivity activity) {
            super(activity);
        }

        public T setTitle(@StringRes int textId) {
            return setTitle(mActivity.getText(textId));
        }

        public T setTitle(@NonNull CharSequence title) {
            this.title = title;
            return (T) this;
        }

        public T setLeftButton(@StringRes int textId, @NonNull OnClickListener listener) {
            return setLeftButton(mActivity.getText(textId), listener);
        }

        public T setLeftButton(@NonNull CharSequence text, @NonNull OnClickListener listener) {
            this.leftText = text;
            this.leftListener = listener;
            return (T) this;
        }

        public T setRightButton(@StringRes int textId, @NonNull OnClickListener listener) {
            return setRightButton(mActivity.getText(textId), listener);
        }

        public T setRightButton(CharSequence text, OnClickListener listener) {
            this.rightText = text;
            this.rightListener = listener;
            return (T) this;
        }
    }
}
