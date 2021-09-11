package com.didi.githubuser.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.didi.githubuser.MainActivity
import com.didi.githubuser.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(mainLooper).postDelayed({
            Intent(this, MainActivity::class.java).also { startActivity(it) }
            finish()
        }, 2000)
    }
}