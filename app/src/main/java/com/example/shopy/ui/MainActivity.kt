package com.example.shopy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import com.example.shopy.R
import com.example.shopy.datalayer.localdatabase.room.WishRoomRepositry
import com.example.shopy.datalayer.onlineDataLayer.NetWorking
import com.example.shopy.datalayer.onlineDataLayer.productDetailsService.ProuductDetailsReposatory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val a = ProuductDetailsReposatory(NetWorking.productDetailsDao)
        val b = WishRoomRepositry(this.application)
        a.getProuduct(6687367364806)
        a.prouductDetaild.observe(this,{
            Log.d("TAG", it.product.title.toString())
            b.saveWishList(it.product)
        })

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.me_menu, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.setting -> {
                true
            }
            R.id.shopping_bag -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}