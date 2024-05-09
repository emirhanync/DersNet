package com.example.otomat

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.otomat.fragments.AnaSayfaFragment
import com.example.otomat.fragments.ProgramFragment
import me.ibrahimsn.lib.SmoothBottomBar


class ogretmen_sayfa : AppCompatActivity() {
    private lateinit var frameLayout: FrameLayout
    private lateinit var bottomNav: SmoothBottomBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ogretmen_sayfa)

        frameLayout = findViewById(R.id.ogretmensayfa_container)
        bottomNav = findViewById(R.id.smoothBarId)
        supportFragmentManager.beginTransaction().replace(R.id.ogretmensayfa_container , AnaSayfaFragment()).addToBackStack(null).commit()

        bottomNav.setOnItemSelectedListener {
            var defaultFragment = Fragment()
            when(it) {
                0 -> defaultFragment = AnaSayfaFragment()
                1 -> defaultFragment = ProgramFragment()
                2 -> defaultFragment = AnaSayfaFragment()
                3 -> defaultFragment = ProgramFragment()
            }
            supportFragmentManager.beginTransaction().replace(R.id.ogretmensayfa_container , defaultFragment).addToBackStack(null).commit()
        }

    }
}