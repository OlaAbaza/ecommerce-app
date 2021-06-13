package com.example.shopy.ui.mainActivity

import android.app.SearchManager
import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopy.R
import com.example.shopy.adapters.CartNotificationAdapter
import com.example.shopy.adapters.WishListIconAdapter
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.ui.search.ShopSearchFragment
import com.example.shopy.util.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {
    private var navHostFragment: Fragment? = null
    private var navController: NavController? = null
    val netwokRecever = NetworkChangeReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.registerReceiver(netwokRecever, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        navController = navHostFragment?.findNavController()
        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController!!)
        }
        setSupportActionBar(toolbar)
        Utils.toolbarImg = toolbar.searchIcon
        Utils.cartView = toolbar.cartView
        supportActionBar?.setDisplayShowTitleEnabled(false)
//        //fav
//        val imageButton = toolbar.findViewById(R.id.favourite) as View

        val wishLiIconAdapter : WishListIconAdapter = WishListIconAdapter(findViewById(R.id.favourite))
        val cartIconAdapter : CartNotificationAdapter = CartNotificationAdapter(findViewById(R.id.cartView))
        val remoteDataSource = RemoteDataSourceImpl()
        val viewModelFactory = ViewModelFactory(
            Repository(
                RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(this.application))
            ), this.application
        )
        val  viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainActivityViewModel::class.java]
        viewModel.getAllWishList().observe(this,{
            Log.d("Tag","${it.size}")
            wishLiIconAdapter.updateView(it.size)
        })
        wishLiIconAdapter.favouriteButton.setOnClickListener {
            navHostFragment = fragment as NavHostFragment
            val graphInflater = (navHostFragment as NavHostFragment).navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.nav_graph)
            navController = (navHostFragment as NavHostFragment).navController
            navGraph.startDestination = R.id.allWishListFragment
            navController!!.graph = navGraph
        }
        viewModel.getAllCartList().observe(this,{
            cartIconAdapter.updateView(it.size)
        })
        cartIconAdapter.favouriteButton.setOnClickListener {
            navHostFragment = fragment as NavHostFragment
            val graphInflater = (navHostFragment as NavHostFragment).navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.nav_graph)
            navController = (navHostFragment as NavHostFragment).navController
            navGraph.startDestination = R.id.cartFragment2
            navController!!.graph = navGraph
        }

        ///////////////////////////////////
        searchIcon.setOnClickListener {
            Toast.makeText(this,"search",Toast.LENGTH_LONG).show()
            navHostFragment = fragment as NavHostFragment
            val graphInflater = (navHostFragment as NavHostFragment).navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.nav_graph)
            navController = (navHostFragment as NavHostFragment).navController
            navGraph.startDestination = R.id.shopSearchFragment
            navController!!.graph = navGraph
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(netwokRecever)
    }
}