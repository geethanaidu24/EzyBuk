package com.atwyn.sys3.ezybuk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class FinalProfile extends AppCompatActivity {
EditText ed1,ed2,ed3,ed4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_profile);
        ed1=(EditText)findViewById(R.id.editText_profilename);
        ed2=(EditText)findViewById(R.id.editText_user2);
        ed3=(EditText)findViewById(R.id.editText_user22);
        ed4=(EditText)findViewById(R.id.editText_user222);


    }
}
