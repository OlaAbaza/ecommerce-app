package com.example.shopy.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopy.R
import com.example.shopy.ui.shopTab.search.ShopSearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      val  bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
//       // val navController = findNavController(R.id.fragment)
//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.shopTabFragment2,R.id.categoryFragment,R.id.meFragment))
//        setupActionBarWithNavController(navController,appBarConfiguration)
//        bottomNavigationView.setupWithNavController(navController)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        val navController = navHostFragment?.findNavController()
        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
       val searchView : SearchView? = searchItem?.actionView as SearchView
       searchView?.setFocusable(true)
     //   searchView?.setIconified(false)

        //searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView?.setOnSearchClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragment, ShopSearchFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }



        return super.onCreateOptionsMenu(menu)
    }

}