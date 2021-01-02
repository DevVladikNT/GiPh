package com.vladiknt.giph

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class AppInfoActivity : AppCompatActivity() {
    var db: FirebaseFirestore? = null

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
}