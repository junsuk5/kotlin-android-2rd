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

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // ③ 권한 요청에 대한 처리를 하는 객체
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // ④ 권한이 허락됨
                getAllPhotos()
            } else {
                Toast.makeText(this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // ① 현재 권한을 체크함
        when (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            PackageManager.PERMISSION_GRANTED -> {
                // ⑤ 권한이 허락됨
                getAllPhotos()
            }
            else -> {
                // ② 권한 요청
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
            while (cursor.moveToNext()) {
                // 사진 정보 id
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                // Uri 얻기
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // 사진의 Uri들 리스트에 담기
                uris.add(contentUri)
            }
        }

        Log.d("MainActivity", "getAllPhotos: $uris")

        // ViewPager2 어댑터 연결
        val adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        adapter.uris = uris

        binding.viewPager.adapter = adapter
    }
}