package com.example.otomat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otomat.R
import com.example.otomat.mvcOgretmenDevam.ModelOgretmenDevam
import com.example.otomat.mvcOgretmenDevam.MyAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class Ogretmen_devam : Fragment() {

    lateinit var rView: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MyAdapter
    lateinit var list: ArrayList<ModelOgretmenDevam>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rView = inflater.inflate(R.layout.fragment_ogretmen_devam, container, false)

        recyclerView = rView.findViewById(R.id.recyclerViewId)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        list = ArrayList()
        var query: Query = FirebaseDatabase.getInstance().getReference("Ogrenciler")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    var modelOgretmenDevam: ModelOgretmenDevam = ModelOgretmenDevam()

                    modelOgretmenDevam.ogrenciAdi = snapshot.child("ogrenciadi").value.toString()
                    modelOgretmenDevam.ogrenciSoyad = snapshot.child("ogrencisoyadi").value.toString()
                    modelOgretmenDevam.ogrenciTc = snapshot.child("ogrencitc").value.toString()

                    list.add(modelOgretmenDevam)
                }
                fetchDevamsizlikCounts()
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        return rView
    }
    private fun fetchDevamsizlikCounts() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("devamsizlik")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (model in list) {
                    val devamsizlikSnapshot = dataSnapshot.child(model.ogrenciTc)
                    val devamsizlikCount = devamsizlikSnapshot.child("count").value?.toString()?.toIntOrNull() ?: 0
                    model.devamsizlik = devamsizlikCount
                }

                // Initialize adapter and set it to RecyclerView
                adapter = MyAdapter(context!!, list)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }
}
