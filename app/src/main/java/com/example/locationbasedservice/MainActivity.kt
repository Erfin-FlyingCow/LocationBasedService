package com.example.locationbasedservice

import android.os.Build
import android.os.Bundle
import android.view.PixelCopy.Request
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService

class MainActivity : AppCompatActivity(), LocationListener {

    private val REQUEST_CODE_ACCESS_FINE_LOCATION = 1
    private val REQUEST_CODE_ACCESS_COARSE_LOCATION = 2
    private lateinit var latitude: TextView
    private lateinit var longitude: TextView

    private var lat: Double = 0.00
    private var long: Double = 0.00

    private var mintime: Long = 0
    private var mindistance: Float = 0.0f

    private val locProvider: String = LocationManager.NETWORK_PROVIDER
    private lateinit var locMGR: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        latitude = findViewById(R.id.latVal)
        longitude = findViewById(R.id.longVal)

        locMGR = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_ACCESS_FINE_LOCATION
                )
            } else {
                startLocationUpdates()
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_CODE_ACCESS_COARSE_LOCATION
                )
            }
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locMGR.requestLocationUpdates(locProvider, mintime, mindistance, this)
            val lastKnownLocation = locMGR.getLastKnownLocation(locProvider)
            if (lastKnownLocation != null) {
                onLocationChanged(lastKnownLocation)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        lat = location.latitude
        long = location.longitude
        latitude.text = lat.toString()
        longitude.text = long.toString()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_ACCESS_FINE_LOCATION || requestCode == REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {

            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }
}
