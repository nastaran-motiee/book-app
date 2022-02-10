package com.example.sellybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sellybook.model.Model;
import com.example.sellybook.model.ModelFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class RegisterUser extends AppCompatActivity {
    private EditText mName, mEmail, mPassword, mPhone;
    private Button mRegisterBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        mName = findViewById(R.id.register_name_et);
        mEmail = findViewById(R.id.register_email_et);
        mPassword = findViewById(R.id.register_password_et);
        mPhone = findViewById(R.id.register_phone_et);
        mRegisterBtn = findViewById(R.id.register_btn);
        progressBar = findViewById(R.id.register_progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError(("Password is Required."));
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //register the user in firebase
                Model.instance.getFireBaseAuthInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterUser.this,"User Created.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginUser.class));

                        } else{
                            Toast.makeText(RegisterUser.this, "Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });







    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }
}