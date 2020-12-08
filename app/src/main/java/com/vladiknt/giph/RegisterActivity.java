package com.vladiknt.giph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.DateInterval;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void registerUser(View view) {
        EditText et = findViewById(R.id.regMail);
        String email = et.getText().toString();
        et = findViewById(R.id.regPass1);
        String pass1 = et.getText().toString();
        et = findViewById(R.id.regPass2);
        String pass2 = et.getText().toString();
        et = findViewById(R.id.regNickname);
        String nickname = et.getText().toString();

        // Проверка на возраст
        et = findViewById(R.id.regAge);
        try {
            int age = Integer.parseInt(et.getText().toString());
            if (age < 18) {
                Toast.makeText(RegisterActivity.this, "Our app is only for adults.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(RegisterActivity.this, "Enter correct age (single number).", Toast.LENGTH_SHORT).show();
            return;
        }

        // Подготовка файлов пользователя для добавления в БД
        Map<String, Object> userLevels = new HashMap<>();
        userLevels.put("hentai", 0);
        userLevels.put("asians", 0);
        userLevels.put("coins", 10);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
        String dateTime = formatter.format(new Date());
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("nickname", nickname);
        userInfo.put("dateJoined", dateTime);
        // Регистрация
        CheckBox license = findViewById(R.id.createAccLicense);
        if (license.isChecked()) {
            if (pass1.equals(pass2)) {
                mAuth.createUserWithEmailAndPassword(email, pass1)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    db.collection("users").document(mAuth.getCurrentUser().getUid()).set(userInfo).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            db.collection("levels").document(mAuth.getCurrentUser().getUid()).set(userLevels).addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful()) {
                                                    mAuth.getCurrentUser().sendEmailVerification();
                                                    Toast.makeText(RegisterActivity.this, "Please, activate your profile in letter.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else
                                                    Toast.makeText(RegisterActivity.this, "Error while adding user levels.", Toast.LENGTH_SHORT).show();
                                            });
                                        } else
                                            Toast.makeText(RegisterActivity.this, "Error while adding user info.", Toast.LENGTH_SHORT).show();
                                    });
                                } else
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                et = findViewById(R.id.regPass1);
                et.clearComposingText();
                et = findViewById(R.id.regPass2);
                et.clearComposingText();
                Toast.makeText(RegisterActivity.this, "Enter your passwords again.", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(RegisterActivity.this, "Please, agree with license.", Toast.LENGTH_SHORT).show();
    }

}