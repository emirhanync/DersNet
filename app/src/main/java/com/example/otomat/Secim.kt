package com.example.otomat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView


class Secim : AppCompatActivity() {

    lateinit var ogrenci: TextView
    lateinit var ogretmen: TextView
    lateinit var logInLayout: LinearLayout
    lateinit var singUpLayout: LinearLayout

    lateinit var girisYap: Button
    lateinit var girisogretmen: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secim)

        ogrenci = findViewById(R.id.logIn)
        ogretmen = findViewById(R.id.singUp)
        logInLayout = findViewById(R.id.logInLayout)
        singUpLayout = findViewById(R.id.singUpLayout)

        girisYap = findViewById(R.id.girisYapBtnId)
        girisogretmen = findViewById(R.id.girisogretmenYapBtnId)

        ogretmen.setOnClickListener {
            ogretmen.background = resources.getDrawable(R.drawable.switch_trcks, null)
            ogretmen.setTextColor(resources.getColor(R.color.textColor, null))
            ogrenci.background = null
            girisYap.visibility = View.GONE
            girisogretmen.visibility = View.VISIBLE
            singUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
            ogrenci.setTextColor(resources.getColor(R.color.dersnet_red, null))
        }
        ogrenci.setOnClickListener {
            ogretmen.background = null
            ogretmen.setTextColor(resources.getColor(R.color.dersnet_red, null))
            ogrenci.background = resources.getDrawable(R.drawable.switch_trcks, null)
            girisYap.visibility = View.VISIBLE
            girisogretmen.visibility = View.GONE
            singUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
            ogrenci.setTextColor(resources.getColor(R.color.textColor, null))
        }


        girisYap.setOnClickListener {
            var intent = Intent(this, Ogrenci_safya::class.java)
            startActivity(intent)
            finish()
        }
        girisogretmen.setOnClickListener {


        }

    }
}