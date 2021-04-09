package com.vladiknt.giph

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class ActivitiesActivity : AppCompatActivity() {

    companion object {
        var lastGame: Long = 0 // Когда последний раз запускали игру
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities)
    }

    fun gameButton(view: View?) {
        if (System.currentTimeMillis() - lastGame > 60000) {
            lastGame = System.currentTimeMillis()
            val game = Intent(this, GameActivity::class.java)
            startActivity(game, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        } else
            Toast.makeText(this, "You can play once a minute", Toast.LENGTH_SHORT).show()
    }

    fun mangaButton(view: View?) {
        val manga = Intent(this, MangaActivity::class.java)
        manga.putExtra("name", "My-daughter-became-a-pornstar") // TODO поменять когда добавлю следующую мангу
        startActivity(manga)
    }

}