package com.example.shopy.adapters

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.shopy.R

class WishListIconAdapter(view: View) {
    var wishListCounter : TextView = view.findViewById<TextView>(R.id.favouriteItems)
    var favouriteButton : ImageButton = view.findViewById<ImageButton>(R.id.favouriteButton)
    fun updateView(number : Int){
        if (number> 0)
        wishListCounter.text = number.toString()
        else
            wishListCounter.visibility = View.INVISIBLE
    }
}