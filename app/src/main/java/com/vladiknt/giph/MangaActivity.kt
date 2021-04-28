package com.vladiknt.giph

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MangaActivity : AppCompatActivity() {

    var storage: FirebaseStorage? = null
    val THREE_MEGABYTES = (3 * 1024 * 1024).toLong()

    companion object {
        var loadedImg: ByteArray? = null // Буффер для изображения
        var numbPages: Int = 0 // Кол-во страниц
        var numbImg: Int = 0 // Номер страницы
        var name: String? = null // Название манги
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga)
        storage = FirebaseStorage.getInstance()
        val iv = findViewById<ImageView>(R.id.mangaImageSrc)
        val tv = findViewById<TextView>(R.id.mangaPages)

        if (name != intent.getStringExtra("name")) {
            name = intent.getStringExtra("name")
            numbImg = 1
            storage!!.reference.child("Manga/$name/$numbImg.jpg").getBytes(THREE_MEGABYTES)
                .addOnSuccessListener { bytesPrm: ByteArray ->
                    loadedImg = bytesPrm
                    val bmp = BitmapFactory.decodeByteArray(bytesPrm, 0, bytesPrm.size)
                    iv.setImageBitmap(bmp)

                    storage!!.reference.child("Manga/$name/pages.txt").getBytes(1024 * 1024)
                        .addOnSuccessListener { bytesPrm: ByteArray ->
                            numbPages = bytesPrm.toString(Charsets.UTF_8).toInt()
                            tv.text = "$numbImg/$numbPages"
                        }
                }
        } else {
            val bmp = BitmapFactory.decodeByteArray(loadedImg, 0, loadedImg!!.size)
            iv.setImageBitmap(bmp)
            tv.text = "$numbImg/$numbPages"
        }
    }

    fun nextButton(view: View?) {
        MainActivity.vibrate()
        storage = FirebaseStorage.getInstance()
        val iv = findViewById<ImageView>(R.id.mangaImageSrc)
        val tv = findViewById<TextView>(R.id.mangaPages)

        if (numbImg < numbPages) {
            numbImg++
            tv.text = "$numbImg/$numbPages"

            storage!!.reference.child("Manga/$name/$numbImg.jpg").getBytes(THREE_MEGABYTES)
                .addOnSuccessListener { bytesPrm: ByteArray ->
                    loadedImg = bytesPrm
                    val bmp = BitmapFactory.decodeByteArray(bytesPrm, 0, bytesPrm.size)
                    iv.setImageBitmap(bmp)
                }
        }
    }

    fun backButton(view: View?) {
        MainActivity.vibrate()
        storage = FirebaseStorage.getInstance()
        val iv = findViewById<ImageView>(R.id.mangaImageSrc)
        val tv = findViewById<TextView>(R.id.mangaPages)

        if (numbImg > 1) {
            numbImg--
            tv.text = "$numbImg/$numbPages"

            storage!!.reference.child("Manga/$name/$numbImg.jpg").getBytes(THREE_MEGABYTES)
                .addOnSuccessListener { bytesPrm: ByteArray ->
                    loadedImg = bytesPrm
                    val bmp = BitmapFactory.decodeByteArray(bytesPrm, 0, bytesPrm.size)
                    iv.setImageBitmap(bmp)
                }
        }
    }

}