package com.vladiknt.giph

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class AppInfoActivity : AppCompatActivity() {

    var db: FirebaseFirestore? = null
    var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)
        val messageInfo = Triple(
            findViewById<TextView>(R.id.appInfoTitle),
            findViewById<TextView>(R.id.appInfoText),
            findViewById<TextView>(R.id.appInfoDate)
        )
        messageInfo.first.clearComposingText()
        messageInfo.second.clearComposingText()
        messageInfo.third.clearComposingText()
        db = FirebaseFirestore.getInstance()
        db!!.collection("system").document("message").get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val messageDoc = task.result
                    messageInfo.first.text = messageDoc!!["title"].toString()
                    messageInfo.second.text = messageDoc["text"].toString()
                    messageInfo.third.text = messageDoc["date"].toString()
                }
            }
    }

    fun checkCode(view: View?) {
        MainActivity.vibrate()
        val code = findViewById<EditText>(R.id.codeText)
        // Получение фотографии по номеру (get/Asian/123)
        if (code.text.toString().startsWith("get/")) {
            db = FirebaseFirestore.getInstance()
            user = FirebaseAuth.getInstance().currentUser
            db!!.collection("levels").document(user!!.uid).get()
                .addOnCompleteListener { task1: Task<DocumentSnapshot?> ->
                    if (task1.isSuccessful) {
                        // Получаем с БД инфо пользователя, чтобы вычесть монетку и добавить опыт
                        val document = task1.result?.data
                        if (document!!["coins"].toString().toInt() > 0) {
                            document["coins"] = document["coins"].toString().toInt() - 1

                            // Формируем запрос картинки
                            val intent = Intent(this, ImageActivity::class.java)
                            val curPh = code.text.split("/").toTypedArray()[2]
                            val curCategory = code.text.split("/").toTypedArray()[1]
                            val path: String
                            when (curCategory) {
                                "Anime" -> {
                                    path = "Anime/$curPh.jpg"
                                    intent.putExtra("path", path)
                                    intent.putExtra("counter", "Hentai #$curPh")
                                    document["hentai"] = document["hentai"].toString().toInt() + 1
                                }
                                "Asian" -> {
                                    path = "Asian/$curPh.jpg"
                                    intent.putExtra("path", path)
                                    intent.putExtra("counter", "Asians #$curPh")
                                    document["asians"] = document["asians"].toString().toInt() + 1
                                }
                                else -> {
                                    Toast.makeText(this, "Incorrect command", Toast.LENGTH_SHORT).show()
                                }
                            }

                            // Записываем изменения в БД и открываем следующую активити, передавая путь для запроса картинки
                            db!!.collection("levels").document(user!!.uid).set(document)
                                .addOnCompleteListener { task2: Task<Void?> ->
                                    if (task2.isSuccessful) {
                                        startActivity(intent)
                                    }
                                }
                        } else Toast.makeText(this, "Not enough coins.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            when (code.text.toString()) {
                // TODO add more codes
                "" -> Toast.makeText(this, "Enter your code firstly", Toast.LENGTH_SHORT).show()
                "code" -> Toast.makeText(this, "Good try))", Toast.LENGTH_SHORT).show()
                "aleno4ka" -> {
                    db = FirebaseFirestore.getInstance()
                    user = FirebaseAuth.getInstance().currentUser
                    db!!.collection("features").document("AlenaPolyakova").get()
                        .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                            if (task.isSuccessful) {
                                // Получаем с базы данных информацию о количестве картинок в соответствующем разделе
                                val maxPhDoc = task.result
                                val maxPhCode = maxPhDoc!!["pictures"].toString().toInt()
                                db!!.collection("levels").document(user!!.uid).get()
                                    .addOnCompleteListener { task1: Task<DocumentSnapshot?> ->
                                        if (task1.isSuccessful) {
                                            val document = task1.result?.data
                                            if (document!!["coins"].toString().toInt() > 0) {
                                                document["coins"] = document["coins"].toString().toInt() - 1

                                                // Формируем запрос картинки
                                                val intent = Intent(this, ImageActivity::class.java)
                                                val curPh: Int = (Math.random() * 1000000).toInt() % maxPhCode + 1
                                                val path = "Feature/AlenaPolyakova/$curPh.jpg"
                                                intent.putExtra("path", path)
                                                intent.putExtra("counter", "Special #$curPh")

                                                // Записываем изменения в БД и открываем следующую активити, передавая путь для запроса картинки
                                                db!!.collection("levels").document(user!!.uid).set(document)
                                                    .addOnCompleteListener { task2: Task<Void?> ->
                                                        if (task2.isSuccessful)
                                                            startActivity(intent)
                                                    }
                                            } else Toast.makeText(
                                                this,
                                                "Not enough coins.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                        }
                }
                "rzemilia" -> {
                    db = FirebaseFirestore.getInstance()
                    user = FirebaseAuth.getInstance().currentUser
                    db!!.collection("features").document("ReZero Emilia").get()
                            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                                if (task.isSuccessful) {
                                    // Получаем с базы данных информацию о количестве картинок в соответствующем разделе
                                    val maxPhDoc = task.result
                                    val maxPhCode = maxPhDoc!!["pictures"].toString().toInt()
                                    db!!.collection("levels").document(user!!.uid).get()
                                            .addOnCompleteListener { task1: Task<DocumentSnapshot?> ->
                                                if (task1.isSuccessful) {
                                                    val document = task1.result?.data
                                                    if (document!!["coins"].toString().toInt() > 0) {
                                                        document["coins"] = document["coins"].toString().toInt() - 1

                                                        // Формируем запрос картинки
                                                        val intent = Intent(this, ImageActivity::class.java)
                                                        val curPh: Int =
                                                                (Math.random() * 1000000).toInt() % maxPhCode + 1
                                                        val path = "Feature/ReZero Emilia/$curPh.jpg"
                                                        intent.putExtra("path", path)
                                                        intent.putExtra("counter", "Special #$curPh")

                                                        // Записываем изменения в БД и открываем следующую активити, передавая путь для запроса картинки
                                                        db!!.collection("levels").document(user!!.uid).set(document)
                                                                .addOnCompleteListener { task2: Task<Void?> ->
                                                                    if (task2.isSuccessful)
                                                                        startActivity(intent)
                                                                }
                                                    } else Toast.makeText(
                                                            this,
                                                            "Not enough coins.",
                                                            Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                }
                            }
                }
                "spaghetti" -> {
                    db = FirebaseFirestore.getInstance()
                    user = FirebaseAuth.getInstance().currentUser
                    db!!.collection("features").document("Spaghetti").get()
                        .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                            if (task.isSuccessful) {
                                // Получаем с базы данных информацию о количестве картинок в соответствующем разделе
                                val maxPhDoc = task.result
                                val maxPhCode = maxPhDoc!!["pictures"].toString().toInt()
                                db!!.collection("levels").document(user!!.uid).get()
                                    .addOnCompleteListener { task1: Task<DocumentSnapshot?> ->
                                        if (task1.isSuccessful) {
                                            val document = task1.result?.data
                                            if (document!!["coins"].toString().toInt() > 0) {
                                                document["coins"] = document["coins"].toString().toInt() - 1

                                                // Формируем запрос картинки
                                                val intent = Intent(this, ImageActivity::class.java)
                                                val curPh: Int =
                                                    (Math.random() * 1000000).toInt() % maxPhCode + 1
                                                val path = "Feature/Spaghetti/$curPh.jpg"
                                                intent.putExtra("path", path)
                                                intent.putExtra("counter", "Special #$curPh")

                                                // Записываем изменения в БД и открываем следующую активити, передавая путь для запроса картинки
                                                db!!.collection("levels").document(user!!.uid).set(document)
                                                    .addOnCompleteListener { task2: Task<Void?> ->
                                                        if (task2.isSuccessful)
                                                            startActivity(intent)
                                                    }
                                            } else Toast.makeText(
                                                this,
                                                "Not enough coins.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                        }
                }
                else -> Toast.makeText(this, "Incorrect code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}