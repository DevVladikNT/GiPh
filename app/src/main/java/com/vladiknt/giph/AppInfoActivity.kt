package com.vladiknt.giph

import android.content.Intent
import android.os.Bundle
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
        val code = findViewById<EditText>(R.id.codeText)
        when(code.text.toString()) {
            // TODO add more codes
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
                                        // Получаем с БД опыт пользователя, чтобы увеличить на 1 в соответствующей категории
                                        val document = task1.result
                                        val expHentai = document!!["hentai"].toString().toInt()
                                        val expAsians = document["asians"].toString().toInt()
                                        var balance = document["coins"].toString().toInt()
                                        if (balance > 0) {
                                            balance--

                                            // Формируем запрос картинки
                                            val intent = Intent(this, ImageActivity::class.java)
                                            val curPh: Int = (Math.random() * 1000000).toInt() % maxPhCode + 1
                                            val path = "Feature/AlenaPolyakova/$curPh.jpg"
                                            intent.putExtra("path", path)
                                            intent.putExtra("counter", "Special #$curPh")

                                            // Записываем изменения в БД и открываем следующую активити, передавая путь для запроса картинки
                                            val userLevels: MutableMap<String, Any> = HashMap()
                                            userLevels["hentai"] = expHentai
                                            userLevels["asians"] = expAsians
                                            userLevels["coins"] = balance
                                            db!!.collection("levels").document(user!!.uid).set(userLevels)
                                                .addOnCompleteListener { task2: Task<Void?> ->
                                                    if (task2.isSuccessful) {
                                                        startActivity(intent)
                                                    }
                                                }
                                        } else Toast.makeText(this, "Not enough coins.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
            }
            else -> Toast.makeText(this, "Incorrect code", Toast.LENGTH_SHORT).show()
        }
    }
}