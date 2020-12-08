package com.vladiknt.giph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            // TODO
        }
    }

    public void registerButton(View view) {
        Intent reg = new Intent(this, RegisterActivity.class);
        startActivity(reg);
    }

    public void resetPasswordButton(View view) {
        EditText et = findViewById(R.id.loginMail);
        if (et.getText().toString().equals(""))
            Toast.makeText(LoginActivity.this, "Enter your email.", Toast.LENGTH_SHORT).show();
        else {
            mAuth.sendPasswordResetEmail(et.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Toast.makeText(LoginActivity.this, "Letter was sent.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(LoginActivity.this, "Incorrect email.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    public void enterButton(View view) {
        EditText et = findViewById(R.id.loginMail);
        String email = et.getText().toString();
        et = findViewById(R.id.loginPass);
        String password = et.getText().toString();
        if (!email.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user.isEmailVerified()) {
                                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(main);
                                } else
                                    Toast.makeText(LoginActivity.this, "Please, check your letters.", Toast.LENGTH_SHORT).show();
                            } else {
                                EditText et = findViewById(R.id.loginPass);
                                et.clearComposingText();
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else
            Toast.makeText(LoginActivity.this, "Please, enter your email.", Toast.LENGTH_SHORT).show();
    }

}