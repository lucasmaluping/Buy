package com.lucas.buy.actiivities;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lucas.buy.R;
import com.lucas.buy.contents.UserContents;
import com.lucas.buy.interfaces.RegistCallBack;
import com.lucas.buy.utils.VolleyUtil;

/**
 * Created by 111 on 2017/7/10.
 */

public class RegistActivity extends Activity implements View.OnClickListener {
    private EditText nameEdit;
    private EditText passwordEdit;
    private EditText ageEdit;
    private EditText genderEdit;
    private Button submitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_layout);

        initView();
    }

    private void initView() {
        nameEdit = (EditText) findViewById(R.id.regist_name_edt);
        passwordEdit = (EditText) findViewById(R.id.regist_password_edt);
        ageEdit = (EditText) findViewById(R.id.regist_age_edt);
        genderEdit = (EditText) findViewById(R.id.regist_gender_edt);
        submitBtn = (Button) findViewById(R.id.regist_submit_btn);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regist_submit_btn:
                String name = nameEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                String age = ageEdit.getText().toString().trim();
                String gender = genderEdit.getText().toString().trim();
                VolleyUtil.getInstance().regist(name,password, age, gender, new RegistCallBack() {
                    @Override
                    public void registSuccess(String success) {
                        if(success.equals(UserContents.error_user_exit)) {
                            Toast.makeText(RegistActivity.this,"用户名已经存在",Toast.LENGTH_SHORT).show();
                        } else if(success.equals(UserContents.ok)) {
                            Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            RegistActivity.this.finish();
                        }
                    }

                    @Override
                    public void registError(String error) {
                        Toast.makeText(RegistActivity.this, "注册不成功",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
