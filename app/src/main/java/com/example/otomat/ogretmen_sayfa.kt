package com.example.otomat

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.otomat.fragments.Ogretmen_anaSayafa_Fragment
import com.example.otomat.fragments.Ogretmen_devam
import com.example.otomat.fragments.Ogretmen_profil

import me.ibrahimsn.lib.SmoothBottomBar


class ogretmen_sayfa : AppCompatActivity() {
    private lateinit var frameLayout: FrameLayout
    private lateinit var bottomNav: SmoothBottomBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ogretmen_sayfa)

        frameLayout = findViewById(R.id.ogretmensayfa_container)
        bottomNav = findViewById(R.id.smoothBarId)

        supportFragmentManager.beginTransaction().replace(R.id.ogretmensayfa_container , Ogretmen_anaSayafa_Fragment()).addToBackStack(null).commit()

        bottomNav.setOnItemSelectedListener {
            var defaultFragment = Fragment()
            when(it) {
                0 -> defaultFragment = Ogretmen_anaSayafa_Fragment()
                1 -> defaultFragment = Ogretmen_devam()
                2 -> defaultFragment = Ogretmen_profil()
            }
            supportFragmentManager.beginTransaction().replace(R.id.ogretmensayfa_container , defaultFragment).addToBackStack(null).commit()
        }

    }
}