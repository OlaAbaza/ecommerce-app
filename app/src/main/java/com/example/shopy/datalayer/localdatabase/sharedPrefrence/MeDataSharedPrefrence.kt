package com.example.shopy.datalayer.localdatabase.sharedPrefrence

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.shopy.ui.StringsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MeDataSharedPrefrence(context: Context) {

    companion object {
        const val fileName = "MeDataSharedPrefrence"
    }

   val points: MutableLiveData<Int> = MutableLiveData<Int>()
    val coupons: MutableLiveData<Int> = MutableLiveData<Int>()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE)

    fun saveMeCoupons(coupons:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val editor: SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putInt(StringsUtils.coupons,coupons)
            editor.apply()
            editor.commit()
        }
    }

    fun saveMePoints(points:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val editor: SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putInt(StringsUtils.points,points)
            editor.apply()
            editor.commit()
        }
    }

    fun loadMeCoupons(){
        CoroutineScope(Dispatchers.IO).launch {
            val coupon = sharedPreferences.getInt(StringsUtils.coupons,1)
            coupons.postValue(coupon)
        }
    }

    fun loadMePoints(){
        CoroutineScope(Dispatchers.IO).launch {
            val point = sharedPreferences.getInt(StringsUtils.points,1)
            points.postValue(point)
        }
    }
}