package com.vladiknt.giph

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage

class AdActivity : AppCompatActivity() {

    var storage: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)

        storage = FirebaseStorage.getInstance()
        val iv: ImageView = findViewById(R.id.adImg)
        val THREE_MEGABYTES = (3 * 1024 * 1024).toLong()
        storage!!.reference.child("Advertisements/current${(Math.random() * 1000000).toInt() % 2 + 1}.jpg").getBytes(THREE_MEGABYTES)
                .addOnSuccessListener { bytesPrm: ByteArray ->
                    val bmp = BitmapFactory.decodeByteArray(bytesPrm, 0, bytesPrm.size)
                    iv.setImageBitmap(bmp)
                    Toast.makeText(this, "You got 10 coins", Toast.LENGTH_SHORT).show()
                }
    }
}