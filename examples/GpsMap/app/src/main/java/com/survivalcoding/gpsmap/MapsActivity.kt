package com.survivalcoding.gpsmap

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.survivalcoding.gpsmap.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    // PloyLine 옵션 ①
    private val polyliOptions = PolylineOptions().width(5f).color(Color.RED)

    // 위치 정보를 얻기위한 객체 ①
    private val fusedLocationProviderClient by lazy {
        FusedLocationProviderClient(this)
    }

    // 위치 요청 정보 ②
    private val locationRequest by lazy {
        LocationRequest.create().apply {
            // GPS 우선
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            // 업데이트 인터벌
            // 위치 정보가 없을 때는 업데이트 안 함
            // 상황에 따라 짧아질 수 있음, 정확하지 않음
            // 다른 앱에서 짧은 인터벌로 위치 정보를 요청하면 짧아질 수 있음
            interval = 10000
            // 정확함. 이것보다 짧은 업데이트는 하지 않음
            fastestInterval = 5000
        }
    }

    // 위치 정보를 얻으면 해야할 행동이 정의된 콜백 객체 ③
    private val locationCallback = MyLocationCallBack()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // 권한이 허락됨 ①
                addLocationListener()
            } else {
                // 권한 거부 ②
                Toast.makeText(this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // 세로 모드로 화면 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SupportMapFragment를 가져와서 지도가 준비되면 알림을 받습니다 ①
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onPause() {
        super.onPause()
        removeLocationListener()
    }

    private fun removeLocationListener() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()

        // 권한 요청 ⑨
        checkPermission(
            cancel = {
                // 위치 정보가 필요한 이유 다이얼로그 표시 ⑩
                showPermissionInfoDialog()
            },
            ok = {
                // 현재 위치를 주기적으로 요청 (권한이 필요한 부분) ⑪
                addLocationListener()
            }
        )
    }

    // ⑤
    @SuppressLint("MissingPermission")
    private fun addLocationListener() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun checkPermission(cancel: () -> Unit, ok: () -> Unit) {   // ①
        // 위치 권한이 없는지 검사
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 허용되지 않음
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // 이전에 권한을 한 번 거부한 적인 있는 경우
                cancel()    // ②
            } else {
                // 권한 요청
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            return
        }
        // 권한을 수락했을 때 실행할 함수
        ok()    // ③
    }

    private fun showPermissionInfoDialog() {
        // 다이얼로그에 권한이 필요한 이유를 설명
        AlertDialog.Builder(this).apply {   // ④
            title = "권한이 필요한 이유"
            setMessage("지도에 위치를 표시하려면 위치 정보 권한이 필요합니다.")
            setPositiveButton("권한 요청") { _, _ ->    // ⑤
                // 권한 요청
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)  // ⑥
            }
            setNegativeButton("거부", null)   // ⑦
        }.show()    // ⑧
    }

    /**
     * 사용 가능한 맵을 조작합니다.
     * 지도를 사용할 준비가 되면 이 콜백이 호출됩니다.
     * 여기서 마커나 선, 청취자를 추가하거나 카메라를 이동할 수 있습니다.
     * 호주 시드니 근처에 마커 추가하고 있습니다.
     * Google Play 서비스가 기기에 설치되어 있지 않은 경우 사용자에게
     * SupportMapFragment 안에 Google Play서비스를 설치하라는 메시지가
     * 표시됩니다. 이 메서드는 사용자가 Google Play 서비스를 설치하고 앱으로
     * 돌아온 후에만 호출(혹은 실행)됩니다.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap    // ②

        // 시드니에 마커를 추가하고 카메라를 이동합니다 ③
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    // ⑥
    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val location = locationResult?.lastLocation

            // ⑦
            location?.run {
                // 17 level로 확대하며 현재 위치로 카메라 이동
                val latLng = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

                Log.d("MapsActivity", "위도: $latitude, 경도: $longitude") // ①

                // PolyLine에 좌표 추가 ②
                polyliOptions.add(latLng)

                // 선 그리기 ③
                mMap.addPolyline(polyliOptions)
            }

        }
    }
}