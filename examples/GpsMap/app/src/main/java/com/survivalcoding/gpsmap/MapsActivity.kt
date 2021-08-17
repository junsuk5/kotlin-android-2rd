package com.survivalcoding.gpsmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.survivalcoding.gpsmap.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SupportMapFragment를 가져와서 지도가 준비되면 알림을 받습니다 ①
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // 위치 정보를 얻기 위한 각종 초기화
    private fun locationInit() {
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        locationCallback = MyLocationCallBack()

        locationRequest = LocationRequest.create().apply {
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

    override fun onResume() {
        super.onResume()
        addLocationListener()
    }

    private fun addLocationListener() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
            locationCallback,
            null);
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

    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val location = locationResult?.lastLocation

            location?.run {
                // 14 level로 확대하며 현재 위치로 카메라 이동
                val latLng = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
            }

        }
    }
}