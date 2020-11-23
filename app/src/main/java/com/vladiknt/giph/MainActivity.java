package com.vladiknt.giph;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void mainAccountButton(View view) {
        Intent account = new Intent(this, AccountActivity.class);
        startActivity(account);
    }

    public void mainCoinsButton(View view) {
        Toast.makeText(this, "Not working yet.", Toast.LENGTH_SHORT).show();
    }

    int expHentai;
    int expAsians;
    public void mainImageButton(View view) {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("levels").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                expHentai = Integer.parseInt(document.get("hentai").toString());
                expAsians = Integer.parseInt(document.get("asians").toString());

                Intent intent = new Intent(this, ImageActivity.class);
                int maxPh, curPh;
                String path;
                switch (view.getId()) {
                    case R.id.mainHentai:
                        maxPh = 5774; // TODO
                        curPh = (int)(Math.random() * 1000000) % maxPh + 1;
                        path = "Anime/" + curPh + ".jpg";
                        intent.putExtra("path", path);
                        intent.putExtra("counter", "Hentai #" + curPh);
                        expHentai++;
                        break;
                    case R.id.mainAsians:
                        maxPh = 376; // TODO
                        curPh = (int)(Math.random() * 1000000) % maxPh + 1;
                        path = "Asian/" + curPh + ".jpg";
                        intent.putExtra("path", path);
                        intent.putExtra("counter", "Asians #" + curPh);
                        expAsians++;
                        break;
                }

                Map<String, Object> userLevels = new HashMap<>();
                userLevels.put("hentai", expHentai);
                userLevels.put("asians", expAsians);
                db.collection("levels").document(user.getUid()).set(userLevels).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        startActivity(intent);
                    }
                });
            }
        });
    }

}