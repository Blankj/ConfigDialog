package com.blankj.dialog.message;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.ecarx.tip.R;
import com.ecarx.tip.dialog.base.BaseContentDialog;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/08/07
 *     desc  : 消息对话框
 * </pre>
 */
public class MessageDialog extends BaseContentDialog<MessageDialog.Config> {

    private TextView tvConfirmMessage;

    @SuppressLint("ValidFragment")
    private MessageDialog() {
    }

    public static MessageDialog newInstance(Config config) {
        Bundle args = new Bundle();
        args.putSerializable(CONFIG_KEY, config);
        MessageDialog fragment = new MessageDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int bindLayout() {
        return R.layout.dialog_xc_message;
    }

    @Override
    protected void initView() {
        tvConfirmMessage = findViewById(R.id.tv_confirm_message);
    }

    @Override
    protected void apply(Config config) {
        tvConfirmMessage.setText(config.message);
    }

    public static class Config extends BaseContentDialog.Config<Config> {

        CharSequence message;

        public Config(FragmentActivity activity) {
            super(activity);
        }

        public Config setMessage(@NonNull CharSequence loadMessage) {
            this.message = loadMessage;
            return this;
        }

        public Config setMessage(@StringRes int textId) {
            this.message = mActivity.getText(textId);
            return this;
        }
    }
}
