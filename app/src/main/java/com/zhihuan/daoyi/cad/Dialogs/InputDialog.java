package com.zhihuan.daoyi.cad.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.databinding.InputDialogBinding;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class InputDialog extends Dialog {

    InputDialogBinding view;
    BackLister lister;
    Context mContext;

    public interface BackLister{
        void getValue(String value);
    }

    public InputDialog(@NonNull Context context,BackLister backLister) {
        super(context);
        this.mContext=context;
        this.lister=backLister;
    }

    public InputDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;
    }

    protected InputDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=InputDialogBinding.inflate(LayoutInflater.from(mContext));
        setContentView(view.getRoot());
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        view.cancel.setOnClickListener(listener);
        view.ok.setOnClickListener(listener);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ok:
                    lister.getValue(view.edit.getText().toString().trim());
                    if(isShowing()){
                        dismiss();
                    }
                    break;
                case R.id.cancel:
                    if(isShowing()){
                        dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
