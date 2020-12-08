package com.vladiknt.giph;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private String APP_VERSION = "0.1.1"; // TODO не забудь обновить перед сборкой

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("system").document("version").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot verDoc = task.getResult();
                if (!verDoc.get("version").equals(APP_VERSION)) {
                    TextView notification = findViewById(R.id.updateNotification);
                    LinearLayout layout = findViewById(R.id.mainLayout);
                    notification.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.INVISIBLE);
                }
            }
        });

        TextView tv = findViewById(R.id.mainCoinsText);
        db.collection("levels").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot verDoc = task.getResult();
                int balance = Integer.parseInt(verDoc.get("coins").toString());
                tv.clearComposingText();
                tv.setText("Coins: " + balance);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView tv = findViewById(R.id.mainCoinsText);
        db.collection("levels").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot verDoc = task.getResult();
                int balance = Integer.parseInt(verDoc.get("coins").toString());
                tv.clearComposingText();
                tv.setText("Coins: " + balance);
            }
        });
    }

    public void mainAppInfoButton(View view) {
        Intent appInfo = new Intent(this, AppInfoActivity.class);
        startActivity(appInfo);
    }

    public void mainAccountButton(View view) {
        Intent account = new Intent(this, AccountActivity.class);
        startActivity(account);
    }

    public void mainCoinsButton(View view) {
        db.collection("levels").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                int expHentai = Integer.parseInt(document.get("hentai").toString());
                int expAsians = Integer.parseInt(document.get("asians").toString());
                int balance = Integer.parseInt(document.get("coins").toString());
                balance += 10;
                Map<String, Object> userLevels = new HashMap<>();
                userLevels.put("hentai", expHentai);
                userLevels.put("asians", expAsians);
                userLevels.put("coins", balance);
                db.collection("levels").document(user.getUid()).set(userLevels).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Intent ad = new Intent(this, AdActivity.class);
                        startActivityForResult(ad, 0);
                    }
                });
            }
        });
    }

    private int expHentai;
    private int expAsians;
    private int maxPhHentai; // Кол-во хентайных фоток в БД
    private int maxPhAsians; // Кол-во фоток азиаток в БД
    private int balance; // Баланс пользователя
    public void mainImageButton(View view) {
        if (view.getId() == R.id.mainSpecial) {
            // TODO доделать спешл картинки
            Toast.makeText(this, "Not working yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("system").document("pictures").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Получаем с базы данных информацию о количестве картинок в соответствующем разделе
                DocumentSnapshot maxPhDoc = task.getResult();
                maxPhHentai = Integer.parseInt(maxPhDoc.get("hentai").toString());
                maxPhAsians = Integer.parseInt(maxPhDoc.get("asians").toString());

                db.collection("levels").document(user.getUid()).get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        // Получаем с БД опыт пользователя, чтобы увеличить на 1 в соответствующей категории
                        DocumentSnapshot document = task1.getResult();
                        expHentai = Integer.parseInt(document.get("hentai").toString());
                        expAsians = Integer.parseInt(document.get("asians").toString());
                        balance = Integer.parseInt(document.get("coins").toString());

                        if (balance > 0) {
                            balance--;

                            // Формируем запрос картинки
                            Intent intent = new Intent(this, ImageActivity.class);
                            int curPh;
                            String path;
                            switch (view.getId()) {
                                case R.id.mainHentai:
                                    curPh = (int) (Math.random() * 1000000) % maxPhHentai + 1;
                                    path = "Anime/" + curPh + ".jpg";
                                    intent.putExtra("path", path);
                                    intent.putExtra("counter", "Hentai #" + curPh);
                                    expHentai++;
                                    break;
                                case R.id.mainAsians:
                                    curPh = (int) (Math.random() * 1000000) % maxPhAsians + 1;
                                    path = "Asian/" + curPh + ".jpg";
                                    intent.putExtra("path", path);
                                    intent.putExtra("counter", "Asians #" + curPh);
                                    expAsians++;
                                    break;
                            }

                            // Записываем изменения в БД и открываем следующую активити, передавая путь для запроса картинки
                            Map<String, Object> userLevels = new HashMap<>();
                            userLevels.put("hentai", expHentai);
                            userLevels.put("asians", expAsians);
                            userLevels.put("coins", balance);
                            db.collection("levels").document(user.getUid()).set(userLevels).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    startActivityForResult(intent, 0);
                                }
                            });
                        } else
                            Toast.makeText(this, "Not enough coins.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}