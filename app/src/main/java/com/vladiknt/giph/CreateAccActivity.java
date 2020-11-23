package com.vladiknt.giph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateAccActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void createAccButton(View view) {
        CheckBox license = findViewById(R.id.createAccLicense);
        if (license.isChecked()) {
            EditText et = findViewById(R.id.createAccNickname);
            Map<String, Object> userData = new HashMap<>();
            userData.put("nickname", et.getText().toString());
            userData.put("dateJoined", new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
            db.collection("users").document(user.getUid()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Map<String, Object> userLevels = new HashMap<>();
                    userLevels.put("asians", 0);
                    userLevels.put("hentai", 0);
                    db.collection("levels").document(user.getUid()).set(userLevels).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CreateAccActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateAccActivity.this, "Error while creating.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateAccActivity.this, "Error while creating.", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(CreateAccActivity.this, "Agree application license.", Toast.LENGTH_SHORT).show();
    }

}