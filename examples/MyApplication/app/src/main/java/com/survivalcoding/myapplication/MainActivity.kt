package com.survivalcoding.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ①
        val clickButton = findViewById<Button>(R.id.clickButton)
        val textView = findViewById<TextView>(R.id.textView)

        clickButton.setOnClickListener {    // ②
            textView.text = "버튼을 눌렀습니다" // ③
        }

        print("10".toInt())
    }
}

fun String.toInt(): Int {
    return Integer.parseInt(this)
}