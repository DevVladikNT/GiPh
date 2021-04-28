package com.vladiknt.giph

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
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
    private val APP_VERSION = "0.1.6" // Текущая версия (сверяется с версией в БД, чтобы показать уведомление при наличии обновления)
    var db: FirebaseFirestore? = null
    var user: FirebaseUser? = null

    companion object {
        var lastAd: Long = 0 // Когда последний раз запускали рекламу
        var vibrator: Vibrator? = null // Для вибрации

        fun vibrate() {
            vibrator?.vibrate(VibrationEffect.createOneShot(3, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
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
        vibrate()
        val appInfo = Intent(this, AppInfoActivity::class.java)
        startActivityForResult(appInfo, 0)
    }

    fun mainAccountButton(view: View?) {
        vibrate()
        val account = Intent(this, AccountActivity::class.java)
        startActivity(account, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    fun activitiesButton(view: View?) {
        vibrate()
        val act = Intent(this, ActivitiesActivity::class.java)
        startActivity(act, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    fun mainCoinsButton(view: View?) {
        vibrate()
        if (System.currentTimeMillis() - lastAd > 60000) {
            lastAd = System.currentTimeMillis()
            db = FirebaseFirestore.getInstance()
            user = FirebaseAuth.getInstance().currentUser
            db!!.collection("levels").document(user!!.uid).get()
                    .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                        if (task.isSuccessful) {
                            val document = task.result?.data
                            document!!["coins"] = document["coins"].toString().toInt() + 10
                            db!!.collection("levels").document(user!!.uid).set(document)
                                    .addOnCompleteListener { task1: Task<Void?> ->
                                        if (task1.isSuccessful) {
                                            val coins = Intent(this, AdActivity::class.java)
                                            startActivityForResult(coins, 0)
                                        }
                                    }
                        }
                    }
        } else
            Toast.makeText(this, "You can watch ad once a minute", Toast.LENGTH_SHORT).show()
    }

    private var maxPhHentai = 0 // Кол-во хентайных фоток в БД
    private var maxPhAsians = 0 // Кол-во фоток азиаток в БД

    fun mainImageButton(view: View) {
        vibrate()
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        db!!.collection("system").document("pictures").get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    // Получаем с базы данных информацию о количестве картинок в соответствующем разделе
                    val maxPhDoc = task.result
                    maxPhHentai = maxPhDoc!!["hentai"].toString().toInt()
                    maxPhAsians = maxPhDoc["asians"].toString().toInt()
                    db!!.collection("levels").document(user!!.uid).get()
                        .addOnCompleteListener { task1: Task<DocumentSnapshot?> ->
                            if (task1.isSuccessful) {
                                // Получаем с БД опыт пользователя, чтобы увеличить на 1 в соответствующей категории
                                val document = task1.result?.data
                                if (document!!["coins"].toString().toInt() > 0) {
                                    document["coins"] = document["coins"].toString().toInt() - 1

                                    // Формируем запрос картинки
                                    val intent = Intent(this, ImageActivity::class.java)
                                    val curPh: Int
                                    val path: String
                                    when (view.id) {
                                        R.id.mainHentai -> {
                                            // Если профилактические работы
                                            if (maxPhHentai == 0) {
                                                Toast.makeText(this, "Not opened now", Toast.LENGTH_SHORT).show()
                                                return@addOnCompleteListener
                                            }
                                            curPh = (Math.random() * 1000000).toInt() % maxPhHentai + 1
                                            path = "Anime/$curPh.jpg"
                                            intent.putExtra("path", path)
                                            intent.putExtra("counter", "Hentai #$curPh")
                                            document["hentai"] = document["hentai"].toString().toInt() + 1
                                        }
                                        R.id.mainAsians -> {
                                            // Если профилактические работы
                                            if (maxPhAsians == 0) {
                                                Toast.makeText(this, "Not opened now", Toast.LENGTH_SHORT).show()
                                                return@addOnCompleteListener
                                            }
                                            curPh = (Math.random() * 1000000).toInt() % maxPhAsians + 1
                                            path = "Asian/$curPh.jpg"
                                            intent.putExtra("path", path)
                                            intent.putExtra("counter", "Asians #$curPh")
                                            document["asians"] = document["asians"].toString().toInt() + 1
                                        }
                                    }

                                    // Записываем изменения в БД и открываем следующую активити, передавая путь для запроса картинки
                                    db!!.collection("levels").document(user!!.uid).set(document)
                                        .addOnCompleteListener { task2: Task<Void?> ->
                                            if (task2.isSuccessful) {
                                                startActivityForResult(intent, 0)
                                            }
                                        }
                                } else Toast.makeText(this, "Not enough coins", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }

}