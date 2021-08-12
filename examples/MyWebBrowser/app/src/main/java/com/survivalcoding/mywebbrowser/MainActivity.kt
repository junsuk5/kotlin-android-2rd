package com.survivalcoding.mywebbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    // 옵션 메뉴 작성
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {                                    // 1
            R.id.action_google, R.id.action_home -> {           // 2
                binding.webView.loadUrl("https://www.google.com")
                return true
            }
            R.id.action_naver -> {                              // 3
                binding.webView.loadUrl("https://www.naver.com")
                return true
            }
            R.id.action_daum -> {                               // 4
                binding.webView.loadUrl("https://www.daum.net")
                return true
            }
            R.id.action_call -> {                               // 5
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:031-123-4567")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {                          // 6
                binding.webView.url?.let { url ->
                    // 문자 보내개
                }
                return true
            }
            R.id.action_email -> {                              // 7
                binding.webView.url?.let { url ->
                    // 이메일 보내기
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)                // 8
    }
}