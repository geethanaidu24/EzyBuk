package com.atwyn.sys3.ezybuk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LogIn extends Fragment {
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
    //final static String moviesUrlAddress = Config.moviesUrlAddress;
TextView tx1;
    EditText ed1,ed2;
    Button b1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.log_in, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ed1=(EditText)view.findViewById(R.id.editText_user);
        ed2=(EditText)view.findViewById(R.id.editText_password);
        b1=(Button)view.findViewById(R.id.signin);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PaymentOptions.class);
                startActivity(intent);
            }
        });
        tx1=(TextView)view.findViewById(R.id.forgot);

        tx1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),ForgotPassword.class);
                startActivity(intent);
            }
        });

    }


}