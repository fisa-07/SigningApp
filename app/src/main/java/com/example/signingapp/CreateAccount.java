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

import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity {
    EditText email,password,confirmpassword;
    Button creataccountbtn;
    ProgressBar progress;
    TextView loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        creataccountbtn = findViewById(R.id.createaccountbtn);
        progress = findViewById(R.id.progress);
        loginbtn = findViewById(R.id.loginbtn);

        creataccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccount.this,LoginAccount.class));
            }
        });

    }

    void createAccount(){
        String emailstring = email.getText().toString();
        String passwordstring = password.getText().toString();
        String confirmpasswordstring = confirmpassword.getText().toString();
        boolean isvalid = validateinfo(emailstring,passwordstring,confirmpasswordstring);
        if(!isvalid){
            return;
        }
        createAccountinFirebase(emailstring,passwordstring);
    }

    void createAccountinFirebase(String emaill,String passwordd){
        setProgressvisible(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(CreateAccount.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Creating account is done
                            Toast.makeText(CreateAccount.this,"Successfully create account,Check email to verify",Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                        }
                        else {
                            // Failure
                            Toast.makeText(CreateAccount.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            setProgressvisible(false);
                        }
                    }
                });
    }

    boolean validateinfo(String emaill,String passwordd, String confirmpasswordd){
        if(!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()){
            email.setError("Email is Invalid");
            return false;
        }
        else if(passwordd.length()<6){
            password.setError("Password length is too short");
            return false;
        }
        else if(!passwordd.equals(confirmpasswordd)){
            confirmpassword.setError("Password not matches");
            return false;
        }
        return true;
    }

    void setProgressvisible(boolean b){
        if(b){
            creataccountbtn.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }
        else{
            creataccountbtn.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }
}