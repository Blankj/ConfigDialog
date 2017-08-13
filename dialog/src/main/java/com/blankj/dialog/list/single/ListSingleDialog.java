package com.blankj.dialog.list.single;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import com.ecarx.tip.R;
import com.ecarx.tip.dialog.base.BaseContentDialog;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/08/10
 *     desc  :
 * </pre>
 */
public class ListSingleDialog extends BaseContentDialog<ListSingleDialog.Config> {

    RecyclerView rvList;

    @SuppressLint("ValidFragment")
    private ListSingleDialog() {
    }

    public static ListSingleDialog newInstance(Config config) {
        Bundle args = new Bundle();
        args.putSerializable(CONFIG_KEY, config);
        ListSingleDialog fragment = new ListSingleDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int bindLayout() {
        return R.layout.dialog_xc_list;
    }

    @Override
    protected void initView() {
        rvList = findViewById(R.id.rv_list);
    }

    @Override
    protected void apply(Config config) {

    }

    public static class Config extends BaseContentDialog.Config<Config> {

        CharSequence[] mItems;
        int mIconId = 0;

        public Config(FragmentActivity activity) {
            super(activity);
        }

        public Config setSingleChoiceItems(CharSequence[] items, int checkedItem) {
            mItems = items;

            return this;
        }
    }

}
