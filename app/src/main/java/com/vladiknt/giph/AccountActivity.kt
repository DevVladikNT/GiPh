package com.vladiknt.giph

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class AccountActivity : AppCompatActivity() {
    var db: FirebaseFirestore? = null
    var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        db!!.collection("users").document(user!!.uid).get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val document = task.result
                    var tv = findViewById<TextView>(R.id.accountNickName)
                    tv.clearComposingText()
                    tv.text = document!!["nickname"].toString()
                    tv = findViewById(R.id.accountDateJoined)
                    tv.clearComposingText()
                    tv.text = "Joined in " + document["dateJoined"].toString()
                }
            }
        db!!.collection("levels").document(user!!.uid).get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val document = task.result
                    val expHentai = document!!["hentai"].toString().toInt()
                    val expAsians = document["asians"].toString().toInt()

                    // Обработка уровней хентая
                    val expBarHentai = arrayOf(
                        findViewById(R.id.h1),
                        findViewById(R.id.h2),
                        findViewById(R.id.h3),
                        findViewById(R.id.h4),
                        findViewById(R.id.h5),
                        findViewById(R.id.h6),
                        findViewById(R.id.h7),
                        findViewById(R.id.h8),
                        findViewById(R.id.h9),
                        findViewById<TextView>(R.id.h10)
                    )
                    var lvlHentai = 1
                    while (true) {
                        if (expHentai >= Math.pow(2.0, lvlHentai.toDouble()))
                            lvlHentai++
                        else
                            break
                    }
                    val lvlH = findViewById<TextView>(R.id.levelHentai)
                    lvlH.clearComposingText()
                    lvlH.text = "$lvlHentai level"
                    if (lvlHentai > 0)
                        printExpBar(expBarHentai, (expHentai - Math.pow(2.0, (lvlHentai - 1).toDouble())) /
                                (Math.pow(2.0, lvlHentai.toDouble()) - Math.pow(2.0, (lvlHentai - 1).toDouble())))
                    else
                        printExpBar(expBarHentai, 0.0)

                    // Обработка уровней азиаточек
                    val expBarAsians = arrayOf(
                        findViewById(R.id.a1),
                        findViewById(R.id.a2),
                        findViewById(R.id.a3),
                        findViewById(R.id.a4),
                        findViewById(R.id.a5),
                        findViewById(R.id.a6),
                        findViewById(R.id.a7),
                        findViewById(R.id.a8),
                        findViewById(R.id.a9),
                        findViewById<TextView>(R.id.a10)
                    )
                    var lvlAsians = 1
                    while (true) {
                        if (expAsians >= Math.pow(2.0, lvlAsians.toDouble()))
                            lvlAsians++
                        else
                            break
                    }
                    val lvlA = findViewById<TextView>(R.id.levelAsians)
                    lvlA.clearComposingText()
                    lvlA.text = "$lvlAsians level"
                    if (lvlAsians > 0) printExpBar(
                        expBarAsians,
                        (expAsians - Math.pow(2.0, (lvlAsians - 1).toDouble())) /
                                (Math.pow(2.0, lvlAsians.toDouble()) - Math.pow(2.0, (lvlAsians - 1).toDouble())))
                    else
                        printExpBar(expBarAsians, 0.0)
                }
            }
    }

    fun signOut(view: View?) {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "You signed out. Close app.", Toast.LENGTH_SHORT).show()
    }

    private fun printExpBar(expBar: Array<TextView>, value: Double) {
        // TODO переделать в соответствии с темой приложения
        for (tv in expBar) {
            tv.setBackgroundColor(resources.getColor(R.color.dayColorPrimaryDark))
        }
        if (value <= 0.05) {
            // ничего не делаем
        } else if (value <= 0.15) {
            expBar[0].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
        } else if (value <= 0.25) {
            expBar[0].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[1].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
        } else if (value <= 0.35) {
            expBar[0].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[1].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[2].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
        } else if (value <= 0.45) {
            expBar[0].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[1].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[2].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[3].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
        } else if (value <= 0.55) {
            expBar[0].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[1].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[2].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[3].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[4].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
        } else if (value <= 0.65) {
            expBar[0].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[1].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[2].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[3].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[4].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[5].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
        } else if (value <= 0.75) {
            expBar[0].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[1].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[2].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[3].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[4].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[5].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[6].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
        } else if (value <= 0.85) {
            expBar[0].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[1].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[2].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[3].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[4].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[5].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[6].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[7].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
        } else if (value <= 0.95) {
            expBar[0].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[1].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[2].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[3].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[4].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[5].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[6].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[7].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            expBar[8].setBackgroundColor(resources.getColor(R.color.dayColorAccent))
        } else {
            for (tv in expBar) {
                tv.setBackgroundColor(resources.getColor(R.color.dayColorAccent))
            }
        }
    }
}