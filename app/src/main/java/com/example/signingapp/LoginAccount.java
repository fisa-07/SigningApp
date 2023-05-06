package com.example.signingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginAccount extends AppCompatActivity {

    EditText email,password;
    Button loginbtn;
    ProgressBar progress;
    TextView createaccountbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);

        email = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginbtn);
        progress = findViewById(R.id.progress);
        createaccountbtn = findViewById(R.id.createaccountbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        createaccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAccount.this,CreateAccount.class));
                finish();
            }
        });
    }

    void loginUser(){
        String emailstring = email.getText().toString();
        String passwordstring = password.getText().toString();
        boolean isvalid = validateinfo(emailstring,passwordstring);
        if(!isvalid){
            return;
        }
        loginAccountinFirebase(emailstring,passwordstring);
    }

    void loginAccountinFirebase(String emaill,String passwordd){
        setProgressvisible(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setProgressvisible(false);
                if(task.isSuccessful()){
                    //loging is successfull
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        // go to mainactivity
                        startActivity(new Intent(LoginAccount.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(LoginAccount.this,"Email is not verified,please go and verify",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //login fails
                    Toast.makeText(LoginAccount.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean validateinfo(String emaill,String passwordd){
        if(!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()){
            email.setError("Email is Invalid");
            return false;
        }
        else if(passwordd.length()<6){
            password.setError("Password length is too short");
            return false;
        }
        return true;
    }

    void setProgressvisible(boolean b){
        if(b){
            loginbtn.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }
        else{
            loginbtn.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }
}