package com.vladiknt.giph

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ImageActivity : AppCompatActivity() {
    var storage: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        storage = FirebaseStorage.getInstance()

        val tv = findViewById<TextView>(R.id.imageCounter)
        tv.clearComposingText()
        tv.text = intent.getStringExtra("counter")

        val iv: ImageView = findViewById(R.id.imageSrc)
        val path = intent.getStringExtra("path")
        val THREE_MEGABYTES = (3 * 1024 * 1024).toLong()
        storage!!.reference.child(path!!).getBytes(THREE_MEGABYTES)
            .addOnSuccessListener { bytesPrm: ByteArray ->
                val bmp = BitmapFactory.decodeByteArray(bytesPrm, 0, bytesPrm.size)
                iv.setImageBitmap(bmp)
            }
    }

    fun showNextButton(view: View?) {
        val tv = findViewById<TextView>(R.id.nextImgButton)
        if (tv.visibility == View.INVISIBLE)
            tv.visibility = View.VISIBLE
        else
            tv.visibility = View.INVISIBLE
    }

    var user: FirebaseUser? = null
    var db: FirebaseFirestore? = null
    private var expHentai = 0
    private var expAsians = 0
    private var maxPhHentai = 0 // Кол-во хентайных фоток в БД
    private var maxPhAsians = 0 // Кол-во фоток азиаток в БД
    private var maxPhSpecial = 0 // Кол-во спец фоток в БД
    private var balance = 0 // Баланс пользователя

    fun reloadImg(view: View?) {
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        db!!.collection("system").document("pictures").get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    // Получаем с базы данных информацию о количестве картинок в соответствующем разделе
                    val maxPhDoc = task.result
                    maxPhHentai = maxPhDoc!!["hentai"].toString().toInt()
                    maxPhAsians = maxPhDoc["asians"].toString().toInt()
                    maxPhSpecial = maxPhDoc["special"].toString().toInt()
                    db!!.collection("levels").document(user!!.uid).get()
                        .addOnCompleteListener { task1: Task<DocumentSnapshot?> ->
                            if (task1.isSuccessful) {
                                // Получаем с БД опыт пользователя, чтобы увеличить на 1 в соответствующей категории
                                val document = task1.result
                                expHentai = document!!["hentai"].toString().toInt()
                                expAsians = document["asians"].toString().toInt()
                                balance = document["coins"].toString().toInt()
                                if (balance > 0) {
                                    balance--

                                    // Формируем запрос картинки
                                    val intent = Intent(this, ImageActivity::class.java)
                                    val curPh: Int
                                    val path: String
                                    when (getIntent().getStringExtra("path")!!.split("/").toTypedArray()[0]) {
                                        "Anime" -> {
                                            curPh = (Math.random() * 1000000).toInt() % maxPhHentai + 1
                                            path = "Anime/$curPh.jpg"
                                            intent.putExtra("path", path)
                                            intent.putExtra("counter", "Hentai #$curPh")
                                            expHentai++
                                        }
                                        "Asian" -> {
                                            curPh = (Math.random() * 1000000).toInt() % maxPhAsians + 1
                                            path = "Asian/$curPh.jpg"
                                            intent.putExtra("path", path)
                                            intent.putExtra("counter", "Asians #$curPh")
                                            expAsians++
                                        }
                                        "Special" -> {
                                            // Если время ивента еще не подошло
                                            if (maxPhSpecial == 0) {
                                                Toast.makeText(this, "Not opened now.", Toast.LENGTH_SHORT).show()
                                                return@addOnCompleteListener
                                            }
                                            curPh = (Math.random() * 1000000).toInt() % maxPhSpecial + 1
                                            path = "Special/$curPh.jpg"
                                            intent.putExtra("path", path)
                                            intent.putExtra("counter", "Special #$curPh")
                                        }
                                    }

                                    // Записываем изменения в БД и открываем следующую активити, передавая путь для запроса картинки
                                    val userLevels: MutableMap<String, Any> = HashMap()
                                    userLevels["hentai"] = expHentai
                                    userLevels["asians"] = expAsians
                                    userLevels["coins"] = balance
                                    db!!.collection("levels").document(user!!.uid).set(userLevels)
                                        .addOnCompleteListener { task2: Task<Void?> ->
                                            if (task2.isSuccessful) {
                                                startActivityForResult(intent, 0)
                                            }
                                        }
                                } else Toast.makeText(this, "Not enough coins.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finish()
    }
}