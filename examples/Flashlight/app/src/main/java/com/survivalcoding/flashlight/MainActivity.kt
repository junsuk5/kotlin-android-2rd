package com.survivalcoding.flashlight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.survivalcoding.flashlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val torch = Torch(this)

        binding.flashSwitch.setOnFocusChangeListener { _, isChecked ->
            if (isChecked) {
                torch.flashOn()
            } else {
                torch.flashOff()
            }
        }
    }
}