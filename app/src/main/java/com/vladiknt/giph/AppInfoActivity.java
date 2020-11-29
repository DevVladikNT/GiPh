package com.vladiknt.giph;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppInfoActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        TextView title = findViewById(R.id.appInfoTitle);
        title.clearComposingText();
        TextView text = findViewById(R.id.appInfoText);
        text.clearComposingText();
        TextView date = findViewById(R.id.appInfoDate);
        date.clearComposingText();
        db = FirebaseFirestore.getInstance();
        db.collection("system").document("message").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot messageDoc = task.getResult();
                title.setText(messageDoc.get("title").toString());
                text.setText(messageDoc.get("text").toString());
                date.setText(messageDoc.get("date").toString());
            }
        });
    }
}