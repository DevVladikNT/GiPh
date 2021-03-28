package com.vladiknt.giph

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    fun goToLicense(view: View?) {
        val lic = Intent(this, LicenseActivity::class.java)
        startActivity(lic)
    }

    fun registerUser(view: View?) {
        var et = findViewById<EditText>(R.id.regMail)
        val email = et.text.toString()
        et = findViewById(R.id.regPass1)
        val pass1 = et.text.toString()
        et = findViewById(R.id.regPass2)
        val pass2 = et.text.toString()
        et = findViewById(R.id.regNickname)
        val nickname = et.text.toString()

        // Проверка на возраст
        et = findViewById(R.id.regAge)
        try {
            val age = et.text.toString().toInt()
            if (age < 18) {
                Toast.makeText(this, "Our app is only for adults.", Toast.LENGTH_SHORT).show()
                return
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Enter correct age (single number).", Toast.LENGTH_SHORT).show()
            return
        }

        // Подготовка файлов пользователя для добавления в БД
        val userLevels: MutableMap<String, Any> = HashMap()
        userLevels["hentai"] = 0
        userLevels["asians"] = 0
        userLevels["coins"] = 10
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.UK)
        val dateTime: String = formatter.format(Date())
        val userInfo: MutableMap<String, Any> = HashMap()
        userInfo["nickname"] = nickname
        userInfo["dateJoined"] = dateTime
        // Регистрация
        val license = findViewById<CheckBox>(R.id.createAccLicense)
        if (license.isChecked) {
            if (pass1 == pass2) {
                mAuth!!.createUserWithEmailAndPassword(email, pass1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            db!!.collection("users").document(mAuth!!.currentUser!!.uid).set(userInfo)
                                .addOnCompleteListener { task1: Task<Void?> ->
                                    if (task1.isSuccessful) {
                                        db!!.collection("levels").document(mAuth!!.currentUser!!.uid).set(userLevels)
                                            .addOnCompleteListener { task2: Task<Void?> ->
                                                if (task2.isSuccessful) {
                                                    mAuth!!.currentUser!!.sendEmailVerification()
                                                    Toast.makeText(this, "Please, activate your profile in letter.", Toast.LENGTH_SHORT).show()
                                                    finish()
                                                } else Toast.makeText(this, "Error while adding user levels.", Toast.LENGTH_SHORT).show()
                                            }
                                    } else Toast.makeText(this, "Error while adding user info.", Toast.LENGTH_SHORT).show()
                                }
                        } else Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                et = findViewById(R.id.regPass1)
                et.clearComposingText()
                et = findViewById(R.id.regPass2)
                et.clearComposingText()
                Toast.makeText(this, "Enter your passwords again.", Toast.LENGTH_SHORT).show()
            }
        } else Toast.makeText(this, "Please, agree with license.", Toast.LENGTH_SHORT).show()
    }
}