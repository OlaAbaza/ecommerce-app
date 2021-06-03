package com.example.shopy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import com.example.shopy.R
import com.example.shopy.datalayer.onlineDataLayer.ProductDetailsServise
import com.example.shopy.datalayer.onlineDataLayer.ProuductDetailsReposatory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val a : ProuductDetailsReposatory = ProuductDetailsReposatory(ProductDetailsServise.productDetailsDao)

        a.getProuduct(6687367823558).observe(this,{
            Log.d("TAG", it.toString())
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