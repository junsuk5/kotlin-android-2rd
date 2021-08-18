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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.survivalcoding.mygallery.databinding.ActivityMainBinding
import kotlin.concurrent.timer

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

        // 권한이 부여되었는지 확인 ①
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 이전에 권한이 허용되지 않음 ②
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // 이전에 이미 권한이 거부되었을 때 설명 ③
                AlertDialog.Builder(this).apply {
                    setTitle("권한이 필요한 이유")
                    setMessage("사진 정보를 얻으려면 외부 저장소 권한이 필요합니다.")
                    setPositiveButton("권한 요청") { _, _ ->
                        // 권한 요청
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)  // ⑥
                    }
                    setNegativeButton("거부", null)
                }.show()    // ⑧
            } else {
                // 권한 요청 ④
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            return
        }
        // 권한이 이미 허용됨 ⑤
        getAllPhotos()
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