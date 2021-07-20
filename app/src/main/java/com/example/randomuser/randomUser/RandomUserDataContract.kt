package com.example.randomuser.randomUser

import com.example.randomuser.network.RandomUserDataClass

/**
 * Created by desiaraj on 18/07/2021
 */

class RandomUserDataContract {

    interface View{
       fun getUserDetail(userdata:List<RandomUserDataClass>)

       fun getUserDataFailure(msg:String)

       fun userClicked(data:RandomUserDataClass)
    }

    interface Presenter{
       fun getRandomUserDetail(count:Int)
    }
}