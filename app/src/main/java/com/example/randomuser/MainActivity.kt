package com.example.randomuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.randomuser.randomUser.RandomUserDataActivity
import com.example.randomuser.weatherReport.WeatherReportActivity

//This is a activity to helps for navigate to weatherReport or Randomuserdata
class MainActivity : AppCompatActivity() {

    //declare button
    lateinit var abuserdata:AppCompatButton
    lateinit var abweatherreport:AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize
        abuserdata = findViewById(R.id.ab_userdata)
        abweatherreport = findViewById(R.id.ab_weatherdata)

        abuserdata.setOnClickListener {
          startActivity(Intent(this, RandomUserDataActivity::class.java))
        }
        abweatherreport.setOnClickListener {
            startActivity(Intent(this,WeatherReportActivity::class.java))
        }
    }
}