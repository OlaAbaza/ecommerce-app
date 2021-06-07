package com.example.shopy.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopy.R
import com.example.shopy.ui.search.ShopSearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private var navHostFragment: Fragment? = null
    private var navController: NavController? = null

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
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //fav
        val imageButton = toolbar.findViewById(R.id.favourite) as ImageButton

        imageButton.setOnClickListener {
            navHostFragment = fragment as NavHostFragment
            val graphInflater = (navHostFragment as NavHostFragment).navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.nav_graph)
            navController = (navHostFragment as NavHostFragment).navController
            navGraph.startDestination = R.id.allWishListFragment
            navController!!.graph = navGraph
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)

        //search
        val searchItem: MenuItem? = menu?.findItem(R.id.search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView
        searchView?.setFocusable(true)
        //searchView?.setIconified(false)
        searchView?.setOnSearchClickListener {
//
//            val manager: FragmentManager = supportFragmentManager
//            val transaction = manager.beginTransaction()
//            transaction.replace(R.id.fragment, ShopSearchFragment())
//            transaction.addToBackStack(null)
//            transaction.commit()

            navHostFragment = fragment as NavHostFragment
            val graphInflater = (navHostFragment as NavHostFragment).navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.nav_graph)
            navController = (navHostFragment as NavHostFragment).navController
            navGraph.startDestination = R.id.shopSearchFragment
            navController!!.graph = navGraph

        }



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cart -> {

                navHostFragment = fragment as NavHostFragment
                val graphInflater = (navHostFragment as NavHostFragment).navController.navInflater
                val navGraph = graphInflater.inflate(R.navigation.nav_graph)
                navController = (navHostFragment as NavHostFragment).navController
                navGraph.startDestination = R.id.cartFragment2
                navController!!.graph = navGraph
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}