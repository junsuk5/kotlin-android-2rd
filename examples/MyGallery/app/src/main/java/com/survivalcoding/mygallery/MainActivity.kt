package com.survivalcoding.mygallery

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.survivalcoding.mygallery.databinding.ActivityMainBinding
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // 권한 체크용
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // 권한이 허락되면
                getAllPhotos()
            } else {
                // 권한이 거부되면
                Toast.makeText(this, "권한이 거부되었음", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        when {
            // 권한이 허락되었다면
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                getAllPhotos()
            }
            else -> {
                // 권한 요청
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun getAllPhotos() {
        val uris = mutableListOf<Uri>()

        // 모든 사진 정보 가져오기
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC" // 찍은 날짜 내림차순
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                uris.add(contentUri)
            }
        }

        Log.d("MainActivity", "fetchPhotos: $uris")

        // ViewPager2 어댑터 연결
        val adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        adapter.uris = uris

        binding.photoPager.adapter = adapter

        // 3초마다 자동 슬라이드
        timer(period = 3000) {
            runOnUiThread {
                with(binding) {
                    if (photoPager.currentItem < adapter.itemCount - 1) {
                        photoPager.currentItem = photoPager.currentItem + 1
                    } else {
                        photoPager.currentItem = 0
                    }
                }
            }
        }
    }
}