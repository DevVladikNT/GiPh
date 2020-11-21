package com.vladiknt.giph;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mainAccountButton(View view) {
        Toast.makeText(this, "Not working yet.", Toast.LENGTH_SHORT).show();
    }

    public void mainCoinsButton(View view) {
        Toast.makeText(this, "Not working yet.", Toast.LENGTH_SHORT).show();
    }

    public void mainImageButton(View view) {
        Intent intent = new Intent(this, ImageActivity.class);
        switch (view.getId()) {
            case R.id.mainHentai:
                intent.putExtra("path", "anime");
                break;
            case R.id.mainAsians:
                intent.putExtra("path", "asian");
                break;
        }
    }

}