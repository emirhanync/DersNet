package com.example.otomat.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.otomat.R
import com.example.otomat.Secim
import com.example.otomat.mvcOgretmenDevam.MyAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Ogrenci_profil : Fragment() {

    lateinit var rView: View
    lateinit var adi: TextView
    lateinit var tc: TextView
    lateinit var devamsizlik: TextView
    lateinit var cikisYapBtnogrId: Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rView = inflater.inflate(R.layout.fragment_ogrenci_profil, container, false)
        adi = rView.findViewById(R.id.ogrenciadi)
        tc = rView.findViewById(R.id.ogrenciaditc)
        devamsizlik = rView.findViewById(R.id.ogrenciDevamsizlik)
        cikisYapBtnogrId = rView.findViewById(R.id.cikisYapBtnogrId) // Initialize the button

        sharedPreferences = activity?.getSharedPreferences("user_session", 0)!!

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Ogrenciler")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    val ogrenciAdi = child.child("ogrenciadi").value?.toString()
                    val ogrenciSoyadi = child.child("ogrencisoyadi").value?.toString()
                    val ogrencitc = child.child("ogrencitc").value?.toString()
                    if (Secim.ogrenciTcStr == ogrencitc) {
                        adi.text = "Öğrenci Adı: $ogrenciAdi\nÖğrenci Soyadı: $ogrenciSoyadi"
                        tc.text = "Öğrenci TC: $ogrencitc"

                        val devamsizlikReference = database.getReference("devamsizlik").child(ogrencitc)
                        devamsizlikReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(devamsizlikSnapshot: DataSnapshot) {
                                devamsizlik.text = "Devamsizlik: " + devamsizlikSnapshot.child("count").value?.toString()
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        cikisYapBtnogrId.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("Çıkış yap")
                .setMessage("Çıkmak istediğine emin misin ?")
                .setPositiveButton("EVET") { dialog, _ ->
                    // Clear user session data
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()

                    // Redirect to login activity
                    val intent = Intent(activity, Secim::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                .setNegativeButton("HAYIR") { dialog, _ ->
                    dialog.cancel()
                }

            val dialog: AlertDialog = alertDialog.create()

            dialog.setOnShowListener {
                val positiveButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setTextColor(Color.parseColor("#FF002E"))
                val negativeButton: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                negativeButton.setTextColor(Color.parseColor("#000000"))
            }
            dialog.show()
        }

        return rView
    }
}
