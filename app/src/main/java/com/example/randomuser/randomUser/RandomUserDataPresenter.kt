package com.example.randomuser.randomUser

import com.example.randomuser.AppConstants
import com.example.randomuser.network.RandomUserDataClient
import com.example.randomuser.network.RandomUserMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * Created by desiaraj on 18/07/2021
 */

class RandomUserDataPresenter(private val view: RandomUserDataContract.View) :
    RandomUserDataContract.Presenter {


    //To get Random Users Info
    override fun getRandomUserDetail(count: Int) {

        RandomUserDataClient().getUserDataService().getUserData(count)
            .enqueue(object : Callback<RandomUserMain> {
                override fun onResponse(
                    call: Call<RandomUserMain>,
                    response: Response<RandomUserMain>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.list?.isNotEmpty()!!) {
                            view.getUserDetail(response.body()!!.list!!)
                        } else {
                            view.getUserDataFailure(AppConstants.Network.APIError)
                        }
                    }
                }

                override fun onFailure(call: Call<RandomUserMain>, t: Throwable) {
                    when (t) {
                        is ConnectException -> {
                            view.getUserDataFailure(AppConstants.Network.NetworkError)
                        }
                        is SocketTimeoutException -> {
                            view.getUserDataFailure(AppConstants.Network.Timeout)
                        }
                        else -> {
                            view.getUserDataFailure(AppConstants.Network.APIError)
                        }
                    }
                }

            })
    }


}