package com.example.shopy.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopy.R
import com.example.shopy.ui.mainActivity.MainActivity
import com.example.shopy.ui.welcomeScreens.WelcomeActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        GlobalScope.launch {
            delay(1500)
            startActivity(Intent(this@SplashActivity,WelcomeActivity::class.java))
            finish()
        }
    }
}