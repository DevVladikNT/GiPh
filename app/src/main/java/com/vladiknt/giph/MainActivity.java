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
        int maxPh, curPh;
        String path;
        switch (view.getId()) {
            case R.id.mainHentai:
                maxPh = 5774; // TODO
                curPh = (int)(Math.random() * 1000000) % maxPh + 1;
                path = "Anime/" + curPh + ".jpg";
                intent.putExtra("path", path);
                intent.putExtra("counter", "Hentai #" + curPh);
                break;
            case R.id.mainAsians:
                maxPh = 376; // TODO
                curPh = (int)(Math.random() * 1000000) % maxPh + 1;
                path = "Asian/" + curPh + ".jpg";
                intent.putExtra("path", path);
                intent.putExtra("counter", "Asians #" + curPh);
                break;
        }
        startActivity(intent);
    }

}