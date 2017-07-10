package com.lucas.buy.actiivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.lucas.buy.MyCallBack;
import com.lucas.buy.R;
import com.lucas.buy.domain.Customer;
import com.lucas.buy.utils.VolleyUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyCallBack{
    private static final String TAG = "MainActivity";
    private EditText nameEdit;
    private EditText passEdit;
    private Button btnLogin;
    private Button btnCancel;
    private String name;
    private String pass;
    private String url = "http://172.16.8.253:8080/LucasWeb2/querysss.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEdit = (EditText) findViewById(R.id.name_edit);
        passEdit = (EditText) findViewById(R.id.pass_edit);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCancel.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                VolleyUtil.getInstance().getVolleyDataGet(url, MainActivity.this);
                name = nameEdit.getText().toString().trim();
                pass = passEdit.getText().toString().trim();

                if (name.equals("lucas") && pass.equals("1234")) {
                    Intent intent = new Intent(this, HomeActivty.class);
                    startActivity(intent);
                }

                break;
            case R.id.btn_cancel:

                break;
        }
    }

    @Override
    public void success(List<Customer> customers) {
        for(Customer customer : customers) {
            Log.i(TAG,"...name:" + customer.getName());
        }
    }

    @Override
    public void error(VolleyError error) {

    }
}
