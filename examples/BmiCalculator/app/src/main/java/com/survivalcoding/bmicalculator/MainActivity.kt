package com.survivalcoding.bmicalculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.survivalcoding.bmicalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadData()

        binding.resultButton.setOnClickListener {
            if (binding.weightEditText.text.isNotBlank() && binding.heightEditText.text.isNotBlank()) {
                // 마지막에 입력한 내용 저장
                saveData(
                    binding.heightEditText.text.toString().toFloat(),
                    binding.weightEditText.text.toString().toFloat(),
                )

                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("weight", binding.weightEditText.text.toString().toFloat())
                    putExtra("height", binding.heightEditText.text.toString().toFloat())
                }
                startActivity(intent)
            }
        }
    }

    private fun saveData(height: Float, weight: Float) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)    // ①
        val editor = pref.edit()    // ②

        editor.putFloat("KEY_HEIGHT", height)    // ③
            .putFloat("KEY_WEIGHT", weight)
            .apply()                   // ④
    }

    private fun loadData() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)  // ①
        val height = pref.getFloat("KEY_HEIGHT", 0f)    // ②
        val weight = pref.getFloat("KEY_WEIGHT", 0f)

        if (height != 0f && weight != 0f) {     // ③
            binding.heightEditText.setText(height.toString())   // ④
            binding.weightEditText.setText(weight.toString())
        }
    }
}