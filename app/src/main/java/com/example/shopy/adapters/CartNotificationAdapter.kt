package com.example.shopy.adapters

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.shopy.R

class CartNotificationAdapter(view: View) {
    var wishListCounter : TextView = view.findViewById<TextView>(R.id.cartItems)
    var favouriteButton : ImageButton = view.findViewById<ImageButton>(R.id.cartButton)
    fun updateView(number : Int){
        if (number> 0) {
            wishListCounter.text = number.toString()
            wishListCounter.visibility = View.VISIBLE
        }
        else
            wishListCounter.visibility = View.INVISIBLE
    }
}