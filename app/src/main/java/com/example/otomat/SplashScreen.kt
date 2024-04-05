package com.example.otomat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Thread {
            Thread.sleep(2500)
            runOnUiThread{
                var i=Intent(this,Secim::class.java)
                startActivity(i)
                finish()
            }

        }.start()

    }

}

