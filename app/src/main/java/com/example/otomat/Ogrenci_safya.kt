package com.example.otomat

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.otomat.fragments.AnaSayfaFragment
import com.example.otomat.fragments.Ogrenci_profil
import com.example.otomat.fragments.ProgramFragment
import me.ibrahimsn.lib.SmoothBottomBar

class Ogrenci_safya : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout
    private lateinit var bottomNav: SmoothBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ogrenci_safya)

        // Initialize
        frameLayout = findViewById(R.id.ogrencisayfa_container)
        bottomNav = findViewById(R.id.smoothBarId)

        // Uygulama ilk acildigi zaman ilk fragment
        supportFragmentManager.beginTransaction().replace(R.id.ogrencisayfa_container , AnaSayfaFragment()).addToBackStack(null).commit()

        bottomNav.setOnItemSelectedListener {
            var defaultFragment = Fragment()
            when(it) {
                0 -> defaultFragment = AnaSayfaFragment()
                1 -> defaultFragment = ProgramFragment()
                2 -> defaultFragment = Ogrenci_profil()

            }
            supportFragmentManager.beginTransaction().replace(R.id.ogrencisayfa_container , defaultFragment).addToBackStack(null).commit()
        }

    }


}