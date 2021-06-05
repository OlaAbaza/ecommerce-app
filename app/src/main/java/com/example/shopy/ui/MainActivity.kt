package com.example.shopy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.example.shopy.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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