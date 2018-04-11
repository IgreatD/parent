package com.sjkj.parent.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;

/**
 * @author by dingl on 2017/12/8.
 */

public class CustomDialogBuilder extends QMUIDialogBuilder {

    private int mLayoutId;
    private View view;

    public CustomDialogBuilder(Context context) {
        super(context);
    }

    public View getView() {
        return view;
    }

    public CustomDialogBuilder setLayout(@LayoutRes int layoutResId) {
        mLayoutId = layoutResId;
        return this;
    }

    @Override
    protected void onCreateContent(QMUIDialog dialog, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        parent.addView(view);
    }
}
