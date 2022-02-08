package com.example.sellybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sellybook.model.ModelFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
public class LoginUser extends AppCompatActivity {

    private TextView mCreateAccount, mForgotPassword;
    private ProgressBar mProgressBar;
    private EditText mEmail, mPassword;
    private Button mLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        mProgressBar = findViewById(R.id.login_progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mEmail = findViewById(R.id.login_email_et);
        mForgotPassword = findViewById(R.id.forgot_pasword_tv);
        mPassword = findViewById(R.id.login_password_et);
        //TODO:--------------
        mLoginBtn = findViewById(R.id.login_button);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
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

                mProgressBar.setVisibility(View.VISIBLE);
                //register the user in firebase
                ModelFireBase.instance.getFirebaseAuthInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginUser.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        } else{
                            Toast.makeText(LoginUser.this, "Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordRessetDialog = new AlertDialog.Builder(v.getContext());
                passwordRessetDialog.setTitle("Reset Password?");
                passwordRessetDialog.setMessage("Enter Your Email To Receive Reset Link.");
                passwordRessetDialog.setView(resetMail);

                passwordRessetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        ModelFireBase.instance.getFirebaseAuthInstance().sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginUser.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginUser.this, "Error! Reset link is Not Sent" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordRessetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                    }
                });

                passwordRessetDialog.create().show();
            }
        });
        mCreateAccount = (TextView) findViewById(R.id.create_account_tv);
        mCreateAccount.setOnClickListener(this::onClick);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void onClick(View v){
        if (v.getId() == R.id.create_account_tv) {
            startActivity(new Intent(this, RegisterUser.class));
        }
    }
}