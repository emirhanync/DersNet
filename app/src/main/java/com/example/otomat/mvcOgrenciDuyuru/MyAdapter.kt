package com.example.otomat.mvcOgrenciDuyuru

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.otomat.R

class MyAdapter (var activity: Activity, var duyuruList: ArrayList<ModelOgrenciDuyuru>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View = LayoutInflater.from(activity).inflate(R.layout.duyuru_carditem, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = duyuruList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var modelDuyuru: ModelOgrenciDuyuru = duyuruList.get(position)

        holder.ogretmenAdi.text =  "${modelDuyuru.ogretmenAdi} ${modelDuyuru.ogretmenSoyadi} - ${modelDuyuru.ogretmenBrans}"
        holder.duyuru.text = modelDuyuru.duyuru
        holder.duyuruTarihi.text = modelDuyuru.duyuruTarihi

    }

    inner class MyViewHolder(iv: View) : RecyclerView.ViewHolder(iv)  {
        var ogretmenAdi: TextView = iv.findViewById(R.id.duyuruCardItem_ogretmenAdiId)
        var duyuru: TextView = iv.findViewById(R.id.duyuruCardItem_duyuruTextId)
        var duyuruTarihi: TextView = iv.findViewById(R.id.duyuruCardItem_tarihId)
    }
}