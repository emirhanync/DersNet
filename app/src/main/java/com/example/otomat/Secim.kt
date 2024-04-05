package com.example.otomat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView


class Secim : AppCompatActivity() {

    lateinit var logIn:TextView
    lateinit var singUp:TextView
    lateinit var logInLayout:LinearLayout
    lateinit var singUpLayout:LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secim)

        logIn=findViewById(R.id.logIn)
        singUp=findViewById(R.id.singUp)
        logInLayout=findViewById(R.id.logInLayout)
        singUpLayout=findViewById(R.id.singUpLayout)

        singUp.setOnClickListener {
            singUp.background = resources.getDrawable(R.drawable.switch_trcks,null)
            singUp.setTextColor(resources.getColor(R.color.textColor,null))
            logIn.background = null
            singUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
            logIn.setTextColor(resources.getColor(R.color.dersnet_red,null))
        }
        logIn.setOnClickListener {
            singUp.background = null
            singUp.setTextColor(resources.getColor(R.color.dersnet_red,null))
            logIn.background = resources.getDrawable(R.drawable.switch_trcks,null)
            singUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
            logIn.setTextColor(resources.getColor(R.color.textColor,null))
        }
//        singIn.setOnClickListener {
//           startActivity(Intent(this@secim,NewActivity::class.java))
//        }
    }
}