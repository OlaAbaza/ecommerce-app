package com.example.shopy.ui.mainActivity

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.shopy.R

class WishListNotificationAdapter(view: View) {
    var wishListCounter : TextView = view.findViewById(R.id.favouriteItems)
    var favouriteButton : ImageButton = view.findViewById(R.id.favouriteButton)
    fun updateView(number : Int){
        if (number> 0){
            wishListCounter.text = number.toString()
            wishListCounter.visibility = View.VISIBLE
        }
        else
            wishListCounter.visibility = View.INVISIBLE
    }
}