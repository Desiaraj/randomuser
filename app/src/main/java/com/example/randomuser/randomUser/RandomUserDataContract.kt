package com.example.randomuser.randomUser

import com.example.randomuser.network.RandomUserDataClass

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