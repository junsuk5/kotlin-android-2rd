package com.survivalcoding.mywebbrowser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.survivalcoding.mywebbrowser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}