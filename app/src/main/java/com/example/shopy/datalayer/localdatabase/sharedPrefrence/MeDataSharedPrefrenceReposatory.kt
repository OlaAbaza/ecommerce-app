package com.example.shopy.datalayer.localdatabase.sharedPrefrence

import android.content.Context
import androidx.lifecycle.MutableLiveData


class MeDataSharedPrefrenceReposatory(context: Context) {
    private val meDataSharedPrefrence = MeDataSharedPrefrence(context)
    val points: MutableLiveData<Int> = MutableLiveData<Int>()
    val coupons: MutableLiveData<Int> = MutableLiveData<Int>()

    fun loadPoints(){
        meDataSharedPrefrence.loadMePoints()
    }

    fun loadCoupons(){
        meDataSharedPrefrence.loadMeCoupons()
    }


    fun saveMeCoupons(coupons:Int){
        meDataSharedPrefrence.saveMeCoupons(coupons)
    }

    fun saveMePoints(points:Int) {
        meDataSharedPrefrence.saveMePoints(points)
    }
}