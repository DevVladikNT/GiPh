package com.vladiknt.giph

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth!!.currentUser
        if (mUser != null) {
            val main = Intent(this, MainActivity::class.java)
            startActivity(main)
        }
        setContentView(R.layout.activity_login)
    }

    fun registerButton(view: View?) {
        val reg = Intent(this, RegisterActivity::class.java)
        startActivity(reg, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    fun resetPasswordButton(view: View?) {
        val et = findViewById<EditText>(R.id.loginMail)
        if (et.text.toString() == "")
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
        else {
            mAuth!!.sendPasswordResetEmail(et.text.toString())
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful)
                        Toast.makeText(this, "Letter was sent", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(this, "Incorrect email", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun enterButton(view: View?) {
        var et = findViewById<EditText>(R.id.loginMail)
        val email = et.text.toString()
        et = findViewById(R.id.loginPass)
        val password = et.text.toString()
        if (email != "") {
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth!!.currentUser
                        if (user!!.isEmailVerified) {
                            val main = Intent(this, MainActivity::class.java)
                            startActivity(main)
                        } else Toast.makeText(this, "Please, check your letters", Toast.LENGTH_SHORT).show()
                    } else {
                        val et = findViewById<EditText>(R.id.loginPass)
                        et.clearComposingText()
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } else Toast.makeText(this, "Please, enter your email", Toast.LENGTH_SHORT).show()
    }
}