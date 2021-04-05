package com.vladiknt.giph

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage

class GameActivity : AppCompatActivity() {
    var storage: FirebaseStorage? = null
    var arr: Array<Array<Char>> = Array(4) { Array(4) { ' ' } }
    var first = ' '
    var second = ' '
    var lastId = 0
    var lastId2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Generating chars
        val listOfChars = listOf('A', 'A', 'B', 'B', 'C', 'C', 'D', 'D', 'E', 'E', 'F', 'F', 'G', 'G', 'H', 'H').toMutableList()
        var index = 0
        for (i in 0..3) {
            for (j in 0..3) {
                index = (Math.random() * 1000000).toInt() % (16 - (i * 4) - j)
                arr[i][j] = listOfChars.removeAt(index)
            }
        }

        // Loading image
        storage = FirebaseStorage.getInstance()
        val iv: ImageView = findViewById(R.id.backgroundGame)
        val THREE_MEGABYTES = (3 * 1024 * 1024).toLong()
        val path = "${if (Math.random() > 0.5) "Anime" else "Asian"}/${(Math.random() * 1000000).toInt() % 100 + 1}.jpg"
        storage!!.reference.child(path).getBytes(THREE_MEGABYTES)
            .addOnSuccessListener { bytesPrm: ByteArray ->
                val bmp = BitmapFactory.decodeByteArray(bytesPrm, 0, bytesPrm.size)
                iv.setImageBitmap(bmp)
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
    }

    fun clickTab(view: View?) {
        val indexes = getIndexes(view!!.id)
        if (first == ' ') {
            first = arr[indexes.first][indexes.second]
            findViewById<TextView>(view.id).text = first.toString()
            lastId = view.id
        } else {
            if (second == ' ') {
                second = arr[indexes.first][indexes.second]
                findViewById<TextView>(view.id).text = second.toString()
                lastId2 = view.id
            } else {
                if (first == second) {
                    findViewById<TextView>(lastId2).visibility = View.INVISIBLE
                    findViewById<TextView>(lastId).visibility = View.INVISIBLE
                } else {
                    findViewById<TextView>(lastId2).text = " "
                    findViewById<TextView>(lastId).text = " "
                }
                first = ' '
                second = ' '
            }
        }
    }

    fun clickLayout(view: View?) {
        if(first != ' ' && second != ' ') {
            if (first == second) {
                findViewById<TextView>(lastId2).visibility = View.INVISIBLE
                findViewById<TextView>(lastId).visibility = View.INVISIBLE
            } else {
                findViewById<TextView>(lastId2).text = " "
                findViewById<TextView>(lastId).text = " "
            }
            first = ' '
            second = ' '
        }
    }

    fun getIndexes(id: Int): Pair<Int, Int> {
        when (id) {
            R.id.cell11 -> return Pair(0, 0)
            R.id.cell12 -> return Pair(0, 1)
            R.id.cell13 -> return Pair(0, 2)
            R.id.cell14 -> return Pair(0, 3)
            R.id.cell21 -> return Pair(1, 0)
            R.id.cell22 -> return Pair(1, 1)
            R.id.cell23 -> return Pair(1, 2)
            R.id.cell24 -> return Pair(1, 3)
            R.id.cell31 -> return Pair(2, 0)
            R.id.cell32 -> return Pair(2, 1)
            R.id.cell33 -> return Pair(2, 2)
            R.id.cell34 -> return Pair(2, 3)
            R.id.cell41 -> return Pair(3, 0)
            R.id.cell42 -> return Pair(3, 1)
            R.id.cell43 -> return Pair(3, 2)
            R.id.cell44 -> return Pair(3, 3)
        }
        return  Pair(0, 0)
    }
}