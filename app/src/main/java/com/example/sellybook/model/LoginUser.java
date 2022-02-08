package com.example.sellybook.model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.sellybook.R;

public class LoginUser extends AppCompatActivity {

    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        register = (TextView) findViewById(R.id.register_tv);
        register.setOnClickListener(this::onClick);

    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.register_tv:
                startActivity(new Intent(this,RegisterUser.class));
                break;
        }
    }
}