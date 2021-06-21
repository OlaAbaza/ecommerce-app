package com.example.shopy.ui.mainActivity

import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var navHostFragment: Fragment? = null
    private var navController: NavController? = null
    lateinit var netwokRecever :NetworkChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        navController = navHostFragment?.findNavController()
        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController!!)
        }

        setSupportActionBar(toolbar)
//        Utils.toolbarImg = toolbar.searchIcon
//        Utils.cartView = toolbar.cartView
        supportActionBar?.setDisplayShowTitleEnabled(false)
        netwokRecever = NetworkChangeReceiver(this)
        this.registerReceiver(netwokRecever, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
//        //fav
//        val imageButton = toolbar.findViewById(R.id.favourite) as View

        val wishLiNotificationAdapter  = WishListNotificationAdapter(findViewById(R.id.favourite))
        val cartIconAdapter  = CartNotificationAdapter(findViewById(R.id.cartView))
        val viewModelFactory = ViewModelFactory(
            RepositoryImpl(
                RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(this.application))
            ), this.application
        )
        val  viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainActivityViewModel::class.java]
        viewModel.getAllWishList().observe(this, {
            wishLiNotificationAdapter.updateView(it.size)
        })
        wishLiNotificationAdapter.favouriteButton.setOnClickListener {
//            navHostFragment = fragment as NavHostFragment
//            val graphInflater = (navHostFragment as NavHostFragment).navController.navInflater
//            val navGraph = graphInflater.inflate(R.navigation.nav_graph)
//            navController = (navHostFragment as NavHostFragment).navController
//            navGraph.startDestination = R.id.allWishListFragment
//            navController!!.graph = navGraph

            val action = NavGraphDirections.actionGlobalAllWishListFragment()
            navController?.navigate(action)
        }
        viewModel.getAllCartList().observe(this, {
            cartIconAdapter.updateView(it.size)
        })
        cartIconAdapter.favouriteButton.setOnClickListener {

            val action = NavGraphDirections.actionGlobalCartFragment2()
            navController?.navigate(action)

        }

        ///////////////////////////////////
        searchIcon.setOnClickListener {
//            Toast.makeText(this,"search",Toast.LENGTH_LONG).show()
//            navHostFragment = fragment as NavHostFragment
//            val graphInflater = (navHostFragment as NavHostFragment).navController.navInflater
//            val navGraph = graphInflater.inflate(R.navigation.nav_graph)
//            navController = (navHostFragment as NavHostFragment).navController
//            navGraph.startDestination = R.id.shopSearchFragment
//            navController!!.graph = navGraph

            val action = NavGraphDirections.actionGlobalShopSearchFragment()
            navController?.navigate(action)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(netwokRecever)
    }
}