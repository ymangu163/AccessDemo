package ym.ha.script.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import ym.ha.script.R;


/**
 * File description
 *
 * @author gao
 * @date 2018/7/16
 */

public class RegisterDialog extends AppCompatDialog implements View.OnClickListener {
    private TextView mOkBtn;
    private Context mContext;

    public RegisterDialog(Context context) {
        this(context, R.style.AppDilogTheme);
    }

    public RegisterDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog_register);

        this.mContext = context;
        setWindowParams();
    }

    //设置Dialog 全屏显示
    private void setWindowParams() {
        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
    }
}
