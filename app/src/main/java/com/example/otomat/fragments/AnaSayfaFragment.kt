package com.example.otomat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otomat.R
import com.example.otomat.mvcOgrenciDuyuru.ModelOgrenciDuyuru
import com.example.otomat.mvcOgrenciDuyuru.MyAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class AnaSayfaFragment : Fragment() {

    private lateinit var fView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var duyuruList: ArrayList<ModelOgrenciDuyuru>
    private lateinit var adapterOfDuyuru: MyAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        fView = inflater.inflate(R.layout.fragment_anasayfa , container , false)

        // Initialize variables
        recyclerView = fView.findViewById(R.id.ogrenci_duyuru_recyclerViewId)

        duyuruProcess()

        return fView
    }

    private fun duyuruProcess() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        duyuruList = ArrayList()
        var query: Query = FirebaseDatabase.getInstance().getReference("Duyurular")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    for (snapshot1: DataSnapshot in snapshot.children) {
                            var modelOgrenciDuyuru = ModelOgrenciDuyuru()

                            modelOgrenciDuyuru.ogretmenAdi = snapshot1.child("ogretmenadi").value.toString()
                            modelOgrenciDuyuru.ogretmenSoyadi = snapshot1.child("ogretmensoyadi").value.toString()
                            modelOgrenciDuyuru.ogretmenBrans = snapshot1.child("ogretmenbrans").value.toString()
                            modelOgrenciDuyuru.duyuruTarihi = snapshot1.child("duyuruTarihi").value.toString()
                            modelOgrenciDuyuru.duyuru = snapshot1.child("duyuru").value.toString()

                            duyuruList.add(modelOgrenciDuyuru)
                    }
                }
                // Initialize adapter and set it to RecyclerView
                adapterOfDuyuru = MyAdapter(requireActivity() , duyuruList)
                recyclerView.adapter = adapterOfDuyuru
                adapterOfDuyuru.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}

