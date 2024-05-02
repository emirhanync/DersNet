package com.example.otomat

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Secim : AppCompatActivity() {

    lateinit var ogrenci: TextView
    lateinit var ogretmen: TextView
    lateinit var logInLayout: LinearLayout
    lateinit var singUpLayout: LinearLayout

    lateinit var girisYap: Button
    lateinit var girisogretmen: Button

    lateinit var ogrenciTcInputLayout: TextInputLayout
    lateinit var ogrenciSifreInputLayout: TextInputLayout
    lateinit var ogretmentcinput: TextInputLayout
    lateinit var ogretmensifrelay: TextInputLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secim)

        ogrenci = findViewById(R.id.logIn)
        ogretmen = findViewById(R.id.singUp)
        logInLayout = findViewById(R.id.logInLayout)
        singUpLayout = findViewById(R.id.singUpLayout)

        ogrenciTcInputLayout = findViewById(R.id.ogrenciTc_textInputId)
        ogrenciSifreInputLayout = findViewById(R.id.ogrenciSifre_textInputId)

        ogretmentcinput = findViewById(R.id.ogretmentc_textinput)
        ogretmensifrelay = findViewById(R.id.ogretmensifre_inputlay)

        girisYap = findViewById(R.id.girisYapBtnId)
        girisogretmen = findViewById(R.id.girisogretmenYapBtnId)

        ogretmen.setOnClickListener {
            ogretmen.background = ResourcesCompat.getDrawable(resources, R.drawable.switch_trcks, null)

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
            ogrenci.background = ResourcesCompat.getDrawable(resources, R.drawable.switch_trcks, null)

            girisYap.visibility = View.VISIBLE
            girisogretmen.visibility = View.GONE
            singUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
            ogrenci.setTextColor(resources.getColor(R.color.textColor, null))
        }


        girisYap.setOnClickListener {
            girisYap_Ogrenci( ogrenciTcInputLayout , ogrenciSifreInputLayout )
        }
        girisogretmen.setOnClickListener {
            girisYap_Ogretmen( ogretmensifrelay , ogretmentcinput )
        }

    }
    private fun girisYap_Ogretmen(ogretmenTc: TextInputLayout , ogretmenSifre: TextInputLayout) {
        val ogretmenTcStr = ogretmenTc.editText!!.text.toString().trim()
        val ogretmenSifreStr = ogretmenSifre.editText!!.text.toString().trim()

        if (ogretmenTcStr.isEmpty() || ogretmenSifreStr.isEmpty()) {
            if (ogretmenTcStr.isEmpty()) {
                Toast.makeText(this, "Lütfen Kimlik numarası girin", Toast.LENGTH_SHORT).show()
            } else if (ogretmenSifreStr.isEmpty()) {
                Toast.makeText(this, "Lütfen şifre girin", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Here, you will check the entered credentials against your Firebase Realtime Database
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Ogretmenler")

            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var found = false
                    for (child in dataSnapshot.children) {
                        val ogretmenTcStrFirebase = child.child("ogretmentc").value?.toString()
                        val ogretmenSifreStrFirebase =
                            child.child("ogretmensifre").value?.toString()

                        if (ogretmenTcStrFirebase != null && ogretmenSifreStrFirebase != null &&
                            ogretmenTcStrFirebase == ogretmenTcStr && ogretmenSifreStrFirebase == ogretmenSifreStr
                        ) {
                            found = true
                            break
                        }
                    }

                    if (found) {
                        // If the credentials match, login successful
                        Toast.makeText(this@Secim, "hoşgeldin.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@Secim, ogretmen_sayfa::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        // If the credentials don't match, show login failed message
                        Toast.makeText(this@Secim, "hatalı şifre veya Tc", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    Toast.makeText(
                        this@Secim,
                        "Error: ${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }
    }
    private fun girisYap_Ogrenci(ogrenciTc: TextInputLayout , ogrenciSifre: TextInputLayout) {
        val ogrenciTcStr = ogrenciTc.editText!!.text.toString().trim()
        val ogrenciSifreStr = ogrenciSifre.editText!!.text.toString().trim()

        if (ogrenciTcStr.isEmpty() || ogrenciSifreStr.isEmpty()) {
            if (ogrenciTcStr.isEmpty()) {
                Toast.makeText(this, "Lütfen Kimlik numarası girin", Toast.LENGTH_SHORT).show()
            } else if (ogrenciSifreStr.isEmpty()) {
                Toast.makeText(this, "Lütfen şifre girin", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Here, you will check the entered credentials against your Firebase Realtime Database
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Ogrenciler")

            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var found = false
                    for (child in dataSnapshot.children) {
                        val ogrenciTcFirebase = child.child("ogrencitc").value?.toString()
                        val ogrenciSifreFirebase = child.child("ogrencisifre").value?.toString()

                        if (ogrenciTcFirebase != null && ogrenciSifreFirebase != null &&
                            ogrenciTcFirebase == ogrenciTcStr && ogrenciSifreFirebase == ogrenciSifreStr) {
                            found = true
                            break
                        }
                    }

                    if (found) {
                        // If the credentials match, login successful
                        Toast.makeText(this@Secim, "hoşgeldin.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@Secim, Ogrenci_safya::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        // If the credentials don't match, show login failed message
                        Toast.makeText(this@Secim, "hatalı şifre veya Tc", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    Toast.makeText(this@Secim, "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}
