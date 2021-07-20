package com.example.randomuser.weatherReport

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.randomuser.AppConstants
import com.example.randomuser.R
import com.example.randomuser.databinding.ActivityWeatherReportBinding
import com.example.randomuser.network.WeatherReportDataClass
import com.google.android.material.snackbar.Snackbar

/**
 * Created by desiaraj on 20/07/2021
 */


class WeatherReportActivity : AppCompatActivity(), LocationListener, WeatherReportContract.View,
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var locationManager: LocationManager;
    lateinit var weatherReportPresenter: WeatherReportPresenter
    lateinit var weatherBinding: ActivityWeatherReportBinding
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var firstTimehit: Boolean = false;
    private var fromRandomUser: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather_report)
        init()
    }

    private fun init() {
        weatherReportPresenter = WeatherReportPresenter(this)
        weatherBinding.swrRefresh.setOnRefreshListener(this)

        fromRandomUser = intent.getBooleanExtra("fromrandomuser", false)
        if (fromRandomUser) {
            lat = intent.getDoubleExtra("lan", 0.0)
            lon = intent.getDoubleExtra("lon", 0.0)
            weatherReportPresenter.getWeatherReport(lat, lon)
        }
        locationPermisson()
    }

    private fun locationPermisson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            } else {
                setPermission()
            }
        } else {
            setPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!firstTimehit) {
            locationPermisson()
        }
    }

    private fun setPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                101
            )
        } else {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.1f, this)
        }

    }

    override fun onLocationChanged(location: Location) {
        //Restrict for change lat and lon for random user
        if (!fromRandomUser) {
            lat = location.latitude;
            lon = location.longitude

            //Restrict for countinues API hit
            if (!firstTimehit) {
                weatherReportPresenter.getWeatherReport(lat, lon)
                firstTimehit = true
            }
        }
    }

    override fun onDestroy() {
        //If location manager is initialized we have stopeed for location updates listen
        if (::locationManager.isInitialized) locationManager.removeUpdates(this)
        super.onDestroy()
    }

    override fun getWeatherReportSuccess(userdata: WeatherReportDataClass?) {
        weatherBinding.swrRefresh.isRefreshing = false
        if (userdata != null) {
            val imageUrl =
                AppConstants.Network.ImagePath.plus(userdata.list?.get(0)?.icon).plus(".png")
            Glide.with(this)
                .load(imageUrl)
                .into(weatherBinding.imgWeatherStatus)
            weatherBinding.tvStatus.text = userdata.list?.get(0)?.main
            weatherBinding.tvCelcius.text = userdata.main.temp.plus(" \u2103")
            weatherBinding.tvSunstartTime.text =
                weatherReportPresenter.getTime(userdata.sys.sunrise!!)
            weatherBinding.tvSunsetTime.text = weatherReportPresenter.getTime(userdata.sys.sunset!!)
            weatherBinding.tvLastupdated.text = getString(R.string.last_updated).plus(" ").plus(
                weatherReportPresenter.getTime(
                    userdata.time!!
                )
            )
            weatherBinding.tvCity.text = userdata.cityName
        }

        weatherBinding.imgSunrise.visibility = View.VISIBLE
        weatherBinding.imgSunset.visibility = View.VISIBLE
        weatherBinding.tvLoading.visibility = View.GONE
    }

    override fun getWeatherReportFailure(msg: String) {
        weatherBinding.swrRefresh.isRefreshing = false
        weatherBinding.tvLoading.text = getString(R.string.error_text)
        Snackbar.make(weatherBinding.tvLoading, msg, 1000).show()
    }

    override fun onRefresh() {
        weatherReportPresenter.getWeatherReport(lat, lon)
    }
}
