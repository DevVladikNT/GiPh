package com.vladiknt.giph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("users").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                TextView tv = findViewById(R.id.accountNickName);
                tv.clearComposingText();
                tv.setText(document.get("nickname").toString());
                tv = findViewById(R.id.accountDateJoined);
                tv.clearComposingText();
                tv.setText("Joined in " + document.get("dateJoined").toString());
            }
        });

        db.collection("levels").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                int expHentai = Integer.parseInt(document.get("hentai").toString());
                int expAsians = Integer.parseInt(document.get("asians").toString());

                // Обработка уровней хентая
                TextView[] expBarHentai = new TextView[]{findViewById(R.id.h1), findViewById(R.id.h2), findViewById(R.id.h3),
                        findViewById(R.id.h4), findViewById(R.id.h5), findViewById(R.id.h6), findViewById(R.id.h7),
                        findViewById(R.id.h8), findViewById(R.id.h9), findViewById(R.id.h10)};
                int lvlHentai = 1;
                while (true) {
                    if (expHentai >= Math.pow(2, lvlHentai))
                        lvlHentai++;
                    else
                        break;
                }
                TextView lvlH = findViewById(R.id.levelHentai);
                lvlH.clearComposingText();
                lvlH.setText("" + lvlHentai + " level");
                if (lvlHentai > 0)
                    printExpBar(expBarHentai, (expHentai - Math.pow(2, lvlHentai - 1)) / (Math.pow(2, lvlHentai) - Math.pow(2, lvlHentai - 1)));
                else
                    printExpBar(expBarHentai, 0);

                // Обработка уровней азиаточек
                TextView[] expBarAsians = new TextView[]{findViewById(R.id.a1), findViewById(R.id.a2), findViewById(R.id.a3),
                        findViewById(R.id.a4), findViewById(R.id.a5), findViewById(R.id.a6), findViewById(R.id.a7),
                        findViewById(R.id.a8), findViewById(R.id.a9), findViewById(R.id.a10)};
                int lvlAsians = 1;
                while (true) {
                    if (expAsians >= Math.pow(2, lvlAsians))
                        lvlAsians++;
                    else
                        break;
                }
                TextView lvlA = findViewById(R.id.levelAsians);
                lvlA.clearComposingText();
                lvlA.setText("" + lvlAsians + " level");
                if (lvlAsians > 0)
                    printExpBar(expBarAsians, (expAsians - Math.pow(2, lvlAsians - 1)) / (Math.pow(2, lvlAsians) - Math.pow(2, lvlAsians - 1)));
                else
                    printExpBar(expBarAsians, 0);
            }
        });
    }

    private void printExpBar(TextView[] expBar, double value) {
        for (TextView tv : expBar) {
            tv.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (value <= 0.05) {
            // ничего не делаем
        } else if (value <= 0.15) {
            expBar[0].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (value <= 0.25) {
            expBar[0].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[1].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (value <= 0.35) {
            expBar[0].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[1].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[2].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (value <= 0.45) {
            expBar[0].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[1].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[2].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[3].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (value <= 0.55) {
            expBar[0].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[1].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[2].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[3].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[4].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (value <= 0.65) {
            expBar[0].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[1].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[2].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[3].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[4].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[5].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (value <= 0.75) {
            expBar[0].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[1].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[2].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[3].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[4].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[5].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[6].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (value <= 0.85) {
            expBar[0].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[1].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[2].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[3].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[4].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[5].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[6].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[7].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (value <= 0.95) {
            expBar[0].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[1].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[2].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[3].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[4].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[5].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[6].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[7].setBackgroundColor(getResources().getColor(R.color.colorAccent));
            expBar[8].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            for (TextView tv : expBar) {
                tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        }
    }

}