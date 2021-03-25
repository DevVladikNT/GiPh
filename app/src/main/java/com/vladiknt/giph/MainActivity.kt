package com.vladiknt.giph

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    // TODO не забудь изменить перед заливкой
    private val APP_VERSION = "0.1.4" // Текущая версия (сверяется с версией в БД, чтобы показать уведомление при наличии обновления)
    var db: FirebaseFirestore? = null
    var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance().currentUser

        // Сверяемся с версией в БД
        db!!.collection("system").document("version").get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val verDoc = task.result
                    if (verDoc!!["version"] != APP_VERSION) {
                        val notification = findViewById<TextView>(R.id.updateNotification)
                        val layout = findViewById<LinearLayout>(R.id.mainLayout)
                        notification.visibility = View.VISIBLE
                        layout.visibility = View.INVISIBLE
                    }
                }
            }

        // Обновляем баланс
        val tv = findViewById<TextView>(R.id.mainCoinsText)
        db!!.collection("levels").document(user!!.uid).get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val verDoc = task.result
                    val balance = verDoc!!["coins"].toString().toInt()
                    tv.clearComposingText()
                    tv.text = "Coins: $balance"
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Обновляем баланс при возвращении в эту Activity
        val tv = findViewById<TextView>(R.id.mainCoinsText)
        db!!.collection("levels").document(user!!.uid).get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val verDoc = task.result
                    val balance = verDoc!!["coins"].toString().toInt()
                    tv.clearComposingText()
                    tv.text = "Coins: $balance"
                }
            }
    }

    fun mainAppInfoButton(view: View?) {
        val appInfo = Intent(this, AppInfoActivity::class.java)
        startActivityForResult(appInfo, 0)
    }

    fun mainAccountButton(view: View?) {
        val account = Intent(this, AccountActivity::class.java)
        startActivity(account)
    }

    fun mainCoinsButton(view: View?) {
        // Функция, которая выдаёт рекламу
        db!!.collection("levels").document(user!!.uid).get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val document = task.result
                    val expHentai = document!!["hentai"].toString().toInt()
                    val expAsians = document["asians"].toString().toInt()
                    var balance = document["coins"].toString().toInt()
                    balance += 10
                    val userLevels: MutableMap<String, Any> = HashMap()
                    userLevels["hentai"] = expHentai
                    userLevels["asians"] = expAsians
                    userLevels["coins"] = balance
                    db!!.collection("levels").document(user!!.uid).set(userLevels)
                        .addOnCompleteListener { task1: Task<Void?> ->
                            if (task1.isSuccessful) {
                                val ad = Intent(this, AdActivity::class.java)
                                startActivityForResult(ad, 0)
                            }
                        }
                }
            }
    }

    private var expHentai = 0
    private var expAsians = 0
    private var maxPhHentai = 0 // Кол-во хентайных фоток в БД
    private var maxPhAsians = 0 // Кол-во фоток азиаток в БД
    private var maxPhSpecial = 0 // Кол-во спец фоток в БД
    private var balance = 0 // Баланс пользователя

    fun mainImageButton(view: View) {
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
                                    when (view.id) {
                                        R.id.mainHentai -> {
                                            curPh = (Math.random() * 1000000).toInt() % maxPhHentai + 1
                                            path = "Anime/$curPh.jpg"
                                            intent.putExtra("path", path)
                                            intent.putExtra("counter", "Hentai #$curPh")
                                            expHentai++
                                        }
                                        R.id.mainAsians -> {
                                            curPh = (Math.random() * 1000000).toInt() % maxPhAsians + 1
                                            path = "Asian/$curPh.jpg"
                                            intent.putExtra("path", path)
                                            intent.putExtra("counter", "Asians #$curPh")
                                            expAsians++
                                        }
                                        R.id.mainSpecial -> {
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

}