package com.vladiknt.giph

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
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

class ImageActivity : AppCompatActivity(), View.OnTouchListener{

    var storage: FirebaseStorage? = null

    companion object {
        var loadedImg: ByteArray? = null // Буффер для изображения
        var pathImg: String? = null // Путь, к которому относится сохраненное изображение
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        findViewById<ImageView>(R.id.imageSrc).setOnTouchListener(this)

        val iv: ImageView = findViewById(R.id.imageSrc)
        val tv = findViewById<TextView>(R.id.imageCounter)
        tv.clearComposingText()
        tv.text = intent.getStringExtra("counter")
        val path = intent.getStringExtra("path")

        storage = FirebaseStorage.getInstance()
        if (!pathImg.equals(path)) {
            val THREE_MEGABYTES = (3 * 1024 * 1024).toLong()
            storage!!.reference.child(path!!).getBytes(THREE_MEGABYTES)
                    .addOnSuccessListener { bytesPrm: ByteArray ->
                        loadedImg = bytesPrm
                        pathImg = path
                        val bmp = BitmapFactory.decodeByteArray(bytesPrm, 0, bytesPrm.size)
                        iv.setImageBitmap(bmp)
                    }
        } else {
            val bmp = BitmapFactory.decodeByteArray(loadedImg, 0, loadedImg!!.size)
            iv.setImageBitmap(bmp)
        }
    }

    // Обрабатывает нажатия пользователя
    var pressTime: Long = 0
    var lastTap: Long = 0
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()

                if (System.currentTimeMillis() - lastTap < 400) {
                    MainActivity.vibrate()
                    reloadImg()
                } else
                    lastTap = System.currentTimeMillis()
            }
            MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - pressTime > 1500) {
                    MainActivity.vibrate()
                    val tvreport = findViewById<TextView>(R.id.reportImgButton)
                    if (tvreport.visibility == View.INVISIBLE)
                        tvreport.visibility = View.VISIBLE
                    else
                        tvreport.visibility = View.INVISIBLE
                }
            }
        }
        return true
    }

    var user: FirebaseUser? = null
    var db: FirebaseFirestore? = null
    private var maxPhHentai = 0 // Кол-во хентайных фоток в БД
    private var maxPhAsians = 0 // Кол-во фоток азиаток в БД

    fun reloadImg() {
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        db!!.collection("system").document("pictures").get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    // Получаем с базы данных информацию о количестве картинок в соответствующем разделе
                    var maxPhDoc = task.result
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
                                    var curPh: Int
                                    var path: String
                                    when (getIntent().getStringExtra("path")!!.split("/").toTypedArray()[0]) {
                                        "Anime" -> {
                                            curPh = (Math.random() * 1000000).toInt() % maxPhHentai + 1
                                            path = "Anime/$curPh.jpg"
                                            intent.putExtra("path", path)
                                            intent.putExtra("counter", "Hentai #$curPh")
                                            document["hentai"] = document["hentai"].toString().toInt() + 1
                                        }
                                        "Asian" -> {
                                            curPh = (Math.random() * 1000000).toInt() % maxPhAsians + 1
                                            path = "Asian/$curPh.jpg"
                                            intent.putExtra("path", path)
                                            intent.putExtra("counter", "Asians #$curPh")
                                            document["asians"] = document["asians"].toString().toInt() + 1
                                        }
                                        // Если вдруг чел ввел код и хочет дальше смотреть спец. фото
                                        "Feature" -> {
                                            val name = getIntent().getStringExtra("path")!!.split("/").toTypedArray()[1]
                                            db!!.collection("features").document(name).get()
                                                    .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                                                        if (task.isSuccessful) {
                                                            // Получаем с базы данных информацию о количестве картинок в соответствующем разделе
                                                            maxPhDoc = task.result
                                                            val maxPhCode = maxPhDoc!!["pictures"].toString().toInt()

                                                            // Формируем запрос картинки
                                                            curPh = (Math.random() * 1000000).toInt() % maxPhCode + 1
                                                            path = "Feature/$name/$curPh.jpg"
                                                            intent.putExtra("path", path)
                                                            intent.putExtra("counter", "Special #$curPh")

                                                            // Записываем изменения в БД и открываем следующую активити, передавая путь для запроса картинки
                                                            db!!.collection("levels").document(user!!.uid).set(document)
                                                                    .addOnCompleteListener { task2: Task<Void?> ->
                                                                        if (task2.isSuccessful) {
                                                                            startActivity(intent)
                                                                            finish()
                                                                        }
                                                                    }
                                                        }
                                                    }
                                            return@addOnCompleteListener
                                        }
                                        else -> {
                                            Toast.makeText(this, "Not working", Toast.LENGTH_SHORT).show()
                                            return@addOnCompleteListener
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

    var reported = false
    fun reportImg(view: View?) {
        if (!reported) {
            user = FirebaseAuth.getInstance().currentUser
            db = FirebaseFirestore.getInstance()
            db!!.collection("levels").document(user!!.uid).get()
                .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                    if (task.isSuccessful) {
                        val info = task.result?.data
                        if (info!!["asians"].toString().toInt() < 128 || info["hentai"].toString().toInt() < 128) {
                            Toast.makeText(this, "You can report with hentai and asians level 8+", Toast.LENGTH_SHORT).show()
                        } else {
                            val imgName = intent.getStringExtra("path").toString()
                            db!!.collection("system").document("reports").get()
                                .addOnCompleteListener { task1: Task<DocumentSnapshot?> ->
                                    if (task1.isSuccessful) {
                                        // TODO Аналогично выпилить куски кода в работе с данными пользователя в других классах
                                        val reports = task1.result?.data
                                        if (reports?.contains(imgName)!!)
                                            reports[imgName] = reports[imgName] as Long + 1
                                        else
                                            reports[imgName] = 1

                                        db!!.collection("system").document("reports").set(reports)
                                            .addOnCompleteListener { task2: Task<Void?> ->
                                                if (task2.isSuccessful) {
                                                    Toast.makeText(
                                                        this,
                                                        "Your report has been sent",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    }
                                }
                            reported = true
                        }
                    }
                }
        } else
            Toast.makeText(this, "You`ve already sent report", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finish()
    }
}
