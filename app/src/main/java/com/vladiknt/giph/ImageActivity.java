package com.vladiknt.giph;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ImageActivity extends AppCompatActivity {

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        storage = FirebaseStorage.getInstance();

        TextView tv = findViewById(R.id.imageCounter);
        tv.clearComposingText();
        tv.setText(getIntent().getStringExtra("counter"));

        ImageView iv = findViewById(R.id.imageSrc);
        String path = getIntent().getStringExtra("path");
        final long THREE_MEGABYTES = 3 * 1024 * 1024;
        storage.getReference().child(path).getBytes(THREE_MEGABYTES).addOnSuccessListener(bytesPrm -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytesPrm, 0, bytesPrm.length);
            iv.setImageBitmap(bmp);
        }).addOnFailureListener(bytesPrm -> {
            //
        });
    }

    public void showNextButton(View view) {
        TextView tv = findViewById(R.id.nextImgButton);
        tv.setVisibility(View.VISIBLE);
    }

    FirebaseUser user;
    FirebaseFirestore db;
    private int expHentai;
    private int expAsians;
    private int maxPhHentai; // Кол-во хентайных фоток в БД
    private int maxPhAsians; // Кол-во фоток азиаток в БД
    private int maxPhSpecial; // Кол-во спец фоток в БД
    private int balance; // Баланс пользователя
    public void reloadImg(View view) {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("system").document("pictures").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Получаем с базы данных информацию о количестве картинок в соответствующем разделе
                DocumentSnapshot maxPhDoc = task.getResult();
                maxPhHentai = Integer.parseInt(maxPhDoc.get("hentai").toString());
                maxPhAsians = Integer.parseInt(maxPhDoc.get("asians").toString());
                maxPhSpecial = Integer.parseInt(maxPhDoc.get("special").toString());

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
                            switch (getIntent().getStringExtra("path").split("/")[0]) {
                                case "Anime":
                                    curPh = (int) (Math.random() * 1000000) % maxPhHentai + 1;
                                    path = "Anime/" + curPh + ".jpg";
                                    intent.putExtra("path", path);
                                    intent.putExtra("counter", "Hentai #" + curPh);
                                    expHentai++;
                                    break;
                                case "Asian":
                                    curPh = (int) (Math.random() * 1000000) % maxPhAsians + 1;
                                    path = "Asian/" + curPh + ".jpg";
                                    intent.putExtra("path", path);
                                    intent.putExtra("counter", "Asians #" + curPh);
                                    expAsians++;
                                    break;
                                case "Special":
                                    // Если время ивента еще не подошло
                                    if (maxPhSpecial == 0) {
                                        Toast.makeText(this, "Not opened now.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    curPh = (int) (Math.random() * 1000000) % maxPhSpecial + 1;
                                    path = "Special/" + curPh + ".jpg";
                                    intent.putExtra("path", path);
                                    intent.putExtra("counter", "Special #" + curPh);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}