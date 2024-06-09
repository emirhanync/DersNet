package com.example.otomat.mvcOgretmenDevam

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.otomat.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyAdapter(var context: Context, var list: ArrayList<ModelOgretmenDevam> ) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.ogretmen_devam_items , parent , false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        var model: ModelOgretmenDevam = list.get(position)
        holder.adi.text = model.ogrenciAdi
        holder.soyad.text = model.ogrenciSoyad
        holder.devamsizlik.text = "Devamsizlik : " + model.devamsizlik.toString()

        holder.devamButon.setOnClickListener {
            // Get Firebase reference
            val databaseReference = FirebaseDatabase.getInstance().reference.child("devamsizlik").child(model.ogrenciTc)

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Read the current count value
                    val currentCount = dataSnapshot.child("count").value?.toString()?.toIntOrNull() ?: 0
                    val newCount = currentCount + 1

                    databaseReference.child("count").setValue(newCount).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            model.devamsizlik = newCount
                            notifyItemChanged(position)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    inner class MyViewHolder(i: View) : RecyclerView.ViewHolder(i) {
        var adi: TextView = i.findViewById(R.id.listeAdi)
        var soyad: TextView = i.findViewById(R.id.listeSoyad)
        var devamsizlik: TextView = i.findViewById(R.id.devamsizlik)
        var devamButon: CardView = i.findViewById(R.id.cardView_Buton)
    }

}