package ym.ha.script;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import ym.ha.script.activity.AppContext;
import ym.ha.script.dialog.RegisterDialog;
import ym.ha.script.utils.AppSharePref;
import ym.ha.script.utils.AppUtil;
import ym.ha.script.utils.ToastUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mRegisterCode;
    private EditText mStartTimeEt;
    private EditText mEndTimeEt;
    private CheckBox mCheckBox;
    private EditText mHahaEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegisterCode = findViewById(R.id.register_et);
        mStartTimeEt = findViewById(R.id.mask_start_tv);
        mEndTimeEt = findViewById(R.id.mask_end_tv);
        mCheckBox = findViewById(R.id.mask_box);
        mHahaEt = findViewById(R.id.haha_id_et);

        findViewById(R.id.open_access_tv).setOnClickListener(this);
        findViewById(R.id.open_haha_tv).setOnClickListener(this);
        findViewById(R.id.get_register_code_tv).setOnClickListener(this);
        findViewById(R.id.register_submit_tv).setOnClickListener(this);
        findViewById(R.id.save_config_tv).setOnClickListener(this);
        mCheckBox.setOnClickListener(this);

        mStartTimeEt.setText(AppSharePref.getInstance().getMaskStartTime());
        mEndTimeEt.setText(AppSharePref.getInstance().getMaskEndTime());
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.open_access_tv) {
            goAccess();
            ToastUtils.showToastForLong(AppContext.getInstance(), "找到哈哈小视频，并打开开关");
        } else if (vId == R.id.open_haha_tv) {
            AppUtil.openApp();
        } else if (vId == R.id.get_register_code_tv) {
            new RegisterDialog(MainActivity.this).show();
        } else if (vId == R.id.register_submit_tv) {
            checkRegisterCode();
        } else if (vId == R.id.save_config_tv) {
            boolean isMask = mCheckBox.isChecked();
            AppSharePref.getInstance().setNightMask(isMask);
            String startTime = mStartTimeEt.getText().toString().trim();
            String endTime = mEndTimeEt.getText().toString().trim();
            AppSharePref.getInstance().setMaskStartTime(startTime);
            AppSharePref.getInstance().setMasEndTime(endTime);
            AppSharePref.getInstance().setHahaId(mHahaEt.getText().toString().trim());
        }

    }

    private void checkRegisterCode() {
        String hahaIdString = mHahaEt.getText().toString().trim();
        String registerCode = mRegisterCode.getText().toString().trim();
        if (TextUtils.isEmpty(hahaIdString)) {
            ToastUtils.showToastForLong(this, "请输入HaHa ID号");
            return;
        }
        if (TextUtils.isEmpty(registerCode)) {
            ToastUtils.showToastForLong(this, "请输入注册码");
            return;
        }
        AppSharePref.getInstance().setHahaId(hahaIdString);

        String codeStr = AppUtil.getMd5(hahaIdString + 36);
        if (TextUtils.isEmpty(codeStr)) {
            ToastUtils.showToastForLong(this, "注册码错误，请重试");
            return;
        }
        codeStr = codeStr.substring(10, 16);
        if (TextUtils.equals(codeStr, registerCode)) {
            AppSharePref.getInstance().setAuthorized(true);
            ToastUtils.showToastForLong(this, "恭喜你，注册成功。现在可以使用脚本了！");
        } else {
            ToastUtils.showToastForLong(this, "注册码错误，请重试");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppUtil.closeKeybord(mRegisterCode);
    }

    /**
     * 前往开启辅助服务界面
     */
    public void goAccess() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
