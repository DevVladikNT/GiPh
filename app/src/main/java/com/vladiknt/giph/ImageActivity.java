package com.vladiknt.giph;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
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
}