package com.example.shopy.ui.welcomeScreens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.shopy.R
import com.example.shopy.ui.mainActivity.MainActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    lateinit var screenPager: ViewPager
    var introViewPagerAdapter: WelcomeViewPagerAdapter? = null
    lateinit var tabIndicator: TabLayout
    var position = 0
    var btnAnim: Animation? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setLayout( WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT)

        // when this activity is about to be launch we need to check if its openened before or not
        if (restorePrefData()) {
            val mainActivity = Intent(applicationContext, MainActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        setContentView(R.layout.activity_welcome)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)

        // fill list screen
        val mList: MutableList<ScreenItem> = ArrayList()
        mList.add(
            ScreenItem(
                "NEW USERS",
                "Get US$10 off if you clicked on ads",
                R.drawable.img4
            )
        )
        mList.add(
            ScreenItem(
                "NEW ARRIVALS",
                " 1000+ new looks refreshed daily!",
                R.drawable.img2
            )
        )
        mList.add(
            ScreenItem(
                "Always New",
                "P R E M I U M C O L L E C T I O N",
                R.drawable.img3
            )
        )
        mList.add(
            ScreenItem(
                "THE FREEDOM\nCHOICE WITH\nSHOPY GIFT\nCARD",
                "",
                R.drawable.img5
            )
        )

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager)
        tabIndicator = findViewById(R.id.tab_indicator)
        introViewPagerAdapter = WelcomeViewPagerAdapter(this, mList)
        screenPager.setAdapter(introViewPagerAdapter)

        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager)

        // next button click Listner
        btn_next.setOnClickListener {
            position = screenPager.getCurrentItem()
            if (position < mList.size) {
                position++
                screenPager.setCurrentItem(position)
            }
            if (position == mList.size - 1) { // when we rech to the last screen

                loaddLastScreen()
            }
        }

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(object : BaseOnTabSelectedListener<TabLayout.Tab > {


            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == mList.size - 1) {
                    loaddLastScreen()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })


        // Get Started button click listener
        btn_get_started.setOnClickListener {

            //open main activity
            val mainActivity = Intent(applicationContext, MainActivity::class.java)
            startActivity(mainActivity)
            // also we need to save a boolean value to storage so next time when the user run the app
            // we could know that he is already checked the intro screen activity
            // i'm going to use shared preferences to that process
            savePrefsData()
            finish()
        }

        // skip button click listener
        tv_skip.setOnClickListener {
            screenPager.setCurrentItem(mList.size)

        }

    }


    fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        return pref.getBoolean("isIntroOpnend", false)
    }

    fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isIntroOpnend", true)
        editor.commit()
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    fun loaddLastScreen() {
        btn_next.setVisibility(View.INVISIBLE)
        btn_get_started.setVisibility(View.VISIBLE)
        tv_skip!!.visibility = View.INVISIBLE
        tab_indicator!!.visibility = View.INVISIBLE
        // setup animation
        btn_get_started?.setAnimation(btnAnim)
    }
}