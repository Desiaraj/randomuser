package com.example.randomuser.randomUser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.randomuser.R
import com.example.randomuser.databinding.FragmentRandomUserDetailBinding
import com.example.randomuser.network.RandomUserDataClass
import com.example.randomuser.weatherReport.WeatherReportActivity

/**
 * Created by desiaraj on 18/07/2021
 */

class RandomUserDetailFragment : Fragment() {

    private lateinit var userDetailBinding: FragmentRandomUserDetailBinding
    private lateinit var userdata: RandomUserDataClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        userDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_random_user_detail,
            container,
            false
        )
        return userDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userdata = arguments?.getParcelable("data")!!

        //set user pic
        Glide.with(this)
            .load(userdata.picture?.large)
            .into(userDetailBinding.civUserPic)

        //set username
        val name: String = userdata.name?.first.plus(" ").plus(userdata.name?.first).plus(" ")
            .plus(userdata.name?.last)
        userDetailBinding.tvName.text = name

        //set user email
        userDetailBinding.tvEmail.text = userdata.email

        //set user address
        userDetailBinding.tvCell.text = userdata.cell

        //set user city
        userDetailBinding.tvCity.text = userdata.location?.city

        //set user state
        userDetailBinding.tvState.text = userdata.location?.state

        //set user country
        userDetailBinding.tvCountry.text = userdata.location?.country

        userDetailBinding.btRandomweather.setOnClickListener {
            startWeatherReportActivity()
        }

    }

    private fun startWeatherReportActivity() {
        val intent =
            Intent(activity as RandomUserDataActivity, WeatherReportActivity::class.java).apply {
                putExtra("fromRandomUser", true)
                putExtra("lan", userdata.location?.coordinates?.lan?.toDouble())
                putExtra("lon", userdata.location?.coordinates?.lon?.toDouble())
            }
        startActivity(intent)
    }


}