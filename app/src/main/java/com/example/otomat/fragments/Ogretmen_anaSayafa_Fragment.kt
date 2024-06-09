package com.example.otomat.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otomat.R
import com.example.otomat.Secim
import com.example.otomat.mvcOgretmenDuyuru.ModelOgretmenDuyuru
import com.example.otomat.mvcOgretmenDuyuru.MyAdapter
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date
import java.util.Locale


class Ogretmen_anaSayafa_Fragment : Fragment() {

    private lateinit var fView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var ekleBtn: Button
    private lateinit var duyuruEditTxt: TextInputLayout
    private lateinit var duyuruList: ArrayList<ModelOgretmenDuyuru>
    private lateinit var adapterOfDuyuru: MyAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fView = inflater.inflate(R.layout.fragment_ogretmen_anasayfa ,  container, false)

        // Initialize variables
        recyclerView = fView.findViewById(R.id.duyuru_recyclerViewId)
        ekleBtn = fView.findViewById(R.id.duyuruEkleBtn)
        duyuruEditTxt = fView.findViewById(R.id.duyuruEditTextId)

        duyuruProcess()

        return fView
    }

    private fun duyuruProcess() {
        if (ekleBtn.isEnabled) {
            //Send Button
            ekleBtn.setOnClickListener {
                var duyuru: String = duyuruEditTxt.editText!!.text.toString()
                if (!duyuru.isEmpty()) {
                    // Hide the keyboard
                    var imm: InputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(duyuruEditTxt.windowToken, 0)

                    var userReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Ogretmenler")
                    userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (snapshot: DataSnapshot in dataSnapshot.children) {
                                    var ogretmenTC: String = snapshot.child("ogretmentc").getValue(String::class.java)!!
                                    var ogretmenAdi: String = snapshot.child("ogretmenadi").getValue(String::class.java)!!
                                    var ogretmenSoyadi: String = snapshot.child("ogretmensoyadi").getValue(String::class.java)!!
                                    var ogretmenBrans: String = snapshot.child("ogretmenbrans").getValue(String::class.java)!!

                                    if (Secim.ogretmenTcStr.equals(ogretmenTC)) {
                                        // Generate a unique key for duyuru
                                        var duyuruId: String = FirebaseDatabase.getInstance().reference.child("Duyurular").child(ogretmenTC).push().key!!

                                        // Get the current timestamp
                                        var timestamp: Long = System.currentTimeMillis()
                                        // Convert the timestamp to a readable date string
                                        var date: String = SimpleDateFormat("yyyy-dd-MM  HH:mm", Locale.getDefault()).format(Date(timestamp))

                                        var duyuruData: HashMap<String, Any> = HashMap()
                                        duyuruData.put("ogretmenadi" , ogretmenAdi)
                                        duyuruData.put("ogretmensoyadi" , ogretmenSoyadi)
                                        duyuruData.put("ogretmenbrans" , ogretmenBrans)
                                        duyuruData.put("duyuru", duyuru)
                                        duyuruData.put("duyuruTarihi", date)

                                        // Store the duyuru in the Duyurular column
                                        FirebaseDatabase.getInstance().reference.child("Duyurular").child(ogretmenTC).child(duyuruId).setValue(duyuruData)

                                        // Clear the EditText after sending the duyuru
                                        duyuruEditTxt.editText!!.setText("")
                                        // Clear the focus after sending the duyuru
                                        duyuruEditTxt.clearFocus()
                                    }
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }
        }
        recyclerView_duyuruProcess()
    }

    private fun recyclerView_duyuruProcess() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        duyuruList = ArrayList()

        var reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Duyurular").child(Secim.ogretmenTcStr)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                duyuruList.clear() // Clear the list to avoid duplicates when onDataChange is called multiple times

                if (snapshot.exists()) {
                    // Duyuru var
                        for (duyuruSnapshot: DataSnapshot in snapshot.children) {
                            var duyuruId: String = duyuruSnapshot.key!! // Get the duyuruId from the key

                            var ogretmenAdi: String = duyuruSnapshot.child("ogretmenadi").getValue(String::class.java)!!
                            var ogretmenSoyadi: String = duyuruSnapshot.child("ogretmensoyadi").getValue(String::class.java)!!
                            var ogretmenBrans: String = duyuruSnapshot.child("ogretmenbrans").getValue(String::class.java)!!
                            var duyuru: String = duyuruSnapshot.child("duyuru").getValue(String::class.java)!!
                            var duyuruTarihi: String = duyuruSnapshot.child("duyuruTarihi").getValue(String::class.java)!!

                            var modelDuyuru: ModelOgretmenDuyuru = ModelOgretmenDuyuru()
                            modelDuyuru.ogretmenAdi = ogretmenAdi
                            modelDuyuru.ogretmenSoyadi = ogretmenSoyadi
                            modelDuyuru.ogretmenBrans = ogretmenBrans
                            modelDuyuru.duyuru = duyuru
                            modelDuyuru.duyuruTarihi = duyuruTarihi

                            //I will send the duyuruId and ogretmenTc too  , cuz i will need them when i want to delete the duyuru
                            modelDuyuru.duyuruId = duyuruId
                            modelDuyuru.ogretmenTC = Secim.ogretmenTcStr

                            duyuruList.add(modelDuyuru)
                        }

                    // Order all comments by timestamp
                    Collections.sort(duyuruList, object : Comparator<ModelOgretmenDuyuru?> {
                        override fun compare(duyuru1: ModelOgretmenDuyuru?, duyuru2: ModelOgretmenDuyuru?): Int {
                            return duyuru2!!.duyuruTarihi.compareTo(duyuru1!!.duyuruTarihi)
                        }
                    })

                    adapterOfDuyuru = MyAdapter(requireActivity() , duyuruList)
                    recyclerView.adapter = adapterOfDuyuru
                    adapterOfDuyuru.notifyDataSetChanged()
                    recyclerView.visibility = View.VISIBLE  // Show RecyclerView
                }
                else {
                    // Duyuru yok
                    recyclerView.visibility = View.GONE  // Hide RecyclerView
                }

            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }


}