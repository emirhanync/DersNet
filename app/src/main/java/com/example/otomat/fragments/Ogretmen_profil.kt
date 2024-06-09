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
import android.widget.Toast
import com.example.otomat.R
import com.example.otomat.Secim
import com.example.otomat.ogretmen_sayfa
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Ogretmen_profil : Fragment() {

    lateinit var rView:View
    lateinit var adi : TextView
    lateinit var tc : TextView
    lateinit var brans : TextView
    lateinit var cikisYapBtn : Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rView = inflater.inflate(R.layout.fragment_ogretmen_profil, container, false)
        adi = rView.findViewById(R.id.ogretmenadi)
        tc=rView.findViewById(R.id.ogretmentc)
        brans=rView.findViewById(R.id.ogretmenbrans)
        cikisYapBtn = rView.findViewById(R.id.cikisYapBtnId)

        sharedPreferences = activity?.getSharedPreferences("user_session", 0)!!

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Ogretmenler")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    val ogretmenAdi = child.child("ogretmenadi").value?.toString()
                    val ogretmenSoyadi = child.child("ogretmensoyadi").value?.toString()
                    val ogretmentc = child.child("ogretmentc").value?.toString()
                    val ogretmenbrans = child.child("ogretmenbrans").value?.toString()
                    if (Secim.ogretmenTcStr.equals(ogretmentc)) {
                        adi.text = "Öğretmen Adı: "+ogretmenAdi + "\n" + "Öğretmen Soyadı:"+ogretmenSoyadi
                        tc.text = "Öğretmen TC:"+ogretmentc
                        brans.text = "Branş:"+ogretmenbrans
                    }
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })


        cikisYapBtn.setOnClickListener {

            var alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("Çıkış yap")
                .setMessage("Çıkmak istediğine emin misin ?")
                .setPositiveButton("EVET", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        // Clear user session data
                        val editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()

                        // Redirect to login activity
                        val intent = Intent(activity, Secim::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                })
            alertDialog.setNegativeButton("HAYIR", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.cancel()
                }
            })

            var dialog: AlertDialog = alertDialog.create()

            dialog.setOnShowListener {
                var positiveButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setTextColor(Color.parseColor("#FF002E"))
                var negativeButton: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                negativeButton.setTextColor(Color.parseColor("#000000"))
            }
            dialog.show()
        }

        return rView
    }


}