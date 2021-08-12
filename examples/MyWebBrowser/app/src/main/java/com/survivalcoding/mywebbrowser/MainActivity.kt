package com.survivalcoding.mywebbrowser

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.survivalcoding.mywebbrowser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 웹뷰 기본 설정
        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    binding.urlEditText.setText(url)
                }
            }
        }

        binding.webView.loadUrl("https://www.google.com")

        binding.urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.webView.loadUrl(binding.urlEditText.text.toString())
                true
            } else {
                false
            }
        }
    }
}