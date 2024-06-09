package com.example.otomat.mvcOgretmenDuyuru

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.otomat.R
import com.example.otomat.Secim
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyAdapter (var activity: Activity, var duyuruList: ArrayList<ModelOgretmenDuyuru>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: View = LayoutInflater.from(activity).inflate(R.layout.duyuru_carditem, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = duyuruList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var modelDuyuru: ModelOgretmenDuyuru = duyuruList.get(position)

        var ogretmenAdiSoyadi = "${modelDuyuru.ogretmenAdi} ${modelDuyuru.ogretmenSoyadi} - ${modelDuyuru.ogretmenBrans}"
        holder.ogretmenAdi.text = ogretmenAdiSoyadi
        holder.duyuru.text = modelDuyuru.duyuru
        holder.duyuruTarihi.text = modelDuyuru.duyuruTarihi

        var ogretmenTc = modelDuyuru.ogretmenTC
        var duyuruId = modelDuyuru.duyuruId

        holder.duyuruBtn.setOnClickListener {
            if (ogretmenTc != null && ogretmenTc.equals(Secim.ogretmenTcStr)) {
                duyuruGuncelle_Silme(activity,duyuruId, ogretmenTc , modelDuyuru.duyuru , ogretmenAdiSoyadi)
            }
        }
    }

    private fun duyuruGuncelle_Silme(activity: Activity, duyuruId: String, ogretmenTc: String, duyuru: String, ogretmenAdiSoyadi: String) {
        var bottomSheetView: View = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_guncelle_silme_duyuru,
            activity.findViewById(R.id.bottomSheetLayout_duyuru_containerId))

        var bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        // Duyuru sil
        var duyuruSilBtn: LinearLayout? = bottomSheetDialog.findViewById(R.id.bottomSheetLayout_duyuruSilmeBtnId)
        duyuruSilBtn!!.setOnClickListener {
            duyuruSilProcess(ogretmenTc , duyuruId , bottomSheetDialog)
        }


        // Duyuru guncelle
        var editBtn: LinearLayout? = bottomSheetDialog.findViewById(R.id.bottomSheetLayout_duyuruGuncellemeBtnId)
        editBtn!!.setOnClickListener {
            duyuruGuncelleProcess(ogretmenTc , duyuruId , duyuru , ogretmenAdiSoyadi , bottomSheetDialog)
        }
    }

    private fun duyuruGuncelleProcess(ogretmenTc: String, duyuruId: String, duyuru: String, ogretmenAdiSoyadi: String, bottomSheetDialog: BottomSheetDialog) {
        if (ogretmenTc != null && ogretmenTc.equals(Secim.ogretmenTcStr)) {
            bottomSheetDialog.dismiss()

            var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            var view: View = activity.layoutInflater.inflate(R.layout.custom_alert_dialog_of_duyuru_guncelle , null)

            // Initialize the variables
            var ogretmenAdiTxtV: TextView = view.findViewById(R.id.customAlertDialogOf_editComment_usernameId)
            var duyuruGuncelleEditTxt : TextInputLayout = view.findViewById(R.id.customAlertDialogOf_editComment_textInputLayoutId)
            var kaydetBtn: CardView = view.findViewById(R.id.customAlertDialogOf_editComment_saveButtonId)
            var iptalBtn: CardView = view.findViewById(R.id.customAlertDialogOf_editComment_cancelButtonId)
            var kaydetBtn_kaydetTxt: TextView = view.findViewById(R.id.customAlertDialogOf_editComment_saveButtonId_textView)
            var kaydetBtn_loadingAnimation: LottieAnimationView = view.findViewById(R.id.customAlertDialogOf_editComment_saveButtonId_loadingLottie)

            // Set the old duyuru to the Edittext
            duyuruGuncelleEditTxt.editText!!.setText(duyuru)
            ogretmenAdiTxtV.text = ogretmenAdiSoyadi

            builder.setView(view)
            var dialog: AlertDialog = builder.create()
            dialog.show()

            // Duyuru Kaydet
            kaydetBtn.setOnClickListener {
                var yeniDuyuru: String = duyuruGuncelleEditTxt.editText!!.text.toString()

                if(yeniDuyuru.isEmpty()) {
                    Toast.makeText(activity, "Lütfen duyuru giriniz", Toast.LENGTH_SHORT).show()
                }
                else {
                    kaydetBtn_kaydetTxt.visibility = View.GONE
                    kaydetBtn_loadingAnimation.visibility = View.VISIBLE
                    // Hide the keyboard
                    var inputMethodManager: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(kaydetBtn.getWindowToken(), 0)
                    fbtan_duyuruGuncelle(duyuruId , yeniDuyuru , dialog , kaydetBtn_kaydetTxt , kaydetBtn_loadingAnimation)
                }
            }

            // for Canceling
            iptalBtn.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun fbtan_duyuruGuncelle(duyuruId: String, yeniDuyuru: String, dialog: AlertDialog, kaydetbtnKaydettxt: TextView, kaydetbtnLoadinganimation: LottieAnimationView) {
        var ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Duyurular").child(Secim.ogretmenTcStr)
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (duyuruSnapshot: DataSnapshot in snapshot.children) {
                    var duyuruKey: String = duyuruSnapshot.key!!
                    if (duyuruKey != null && duyuruKey.equals(duyuruId)) {
                        if (duyuruSnapshot.hasChild("duyuru")) {
                            // Eski duyuru yeni duyuruya guncelle
                            duyuruSnapshot.child("duyuru").ref.setValue(yeniDuyuru)
                            Toast.makeText(activity, "Duyuru Güncellendi!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()

                            kaydetbtnKaydettxt.visibility = View.VISIBLE
                            kaydetbtnLoadinganimation.visibility = View.GONE
                        }
                        return
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }

    private fun duyuruSilProcess(ogretmenTc: String, duyuruId: String , bottomSheetDialog: BottomSheetDialog) {
        if (ogretmenTc != null && ogretmenTc.equals(Secim.ogretmenTcStr)) {
            var alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("Silme")
                .setMessage("Silmek istediğine emin misin ?")
                .setPositiveButton("Sil", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        fbtan_duyuruSil(duyuruId , bottomSheetDialog)
                    }
                })
            alertDialog.setNegativeButton("Iptal", object: DialogInterface.OnClickListener {
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
    }

    private fun fbtan_duyuruSil(duyuruId: String, bottomSheetDialog: BottomSheetDialog) {

        var ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Duyurular").child(Secim.ogretmenTcStr)
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (duyuruSnapshot: DataSnapshot in snapshot.children) {
                    var duyuruKey: String = duyuruSnapshot.key!!
                    if (duyuruKey != null && duyuruKey.equals(duyuruId)) {
                        duyuruSnapshot.ref.removeValue()
                        Toast.makeText(activity, "Duyuru silindi!", Toast.LENGTH_SHORT).show()
                        bottomSheetDialog.dismiss()
                        return
                    }
                }
                bottomSheetDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    inner class MyViewHolder(iv: View) : RecyclerView.ViewHolder(iv)  {
        var ogretmenAdi: TextView = iv.findViewById(R.id.duyuruCardItem_ogretmenAdiId)
        var duyuru: TextView = iv.findViewById(R.id.duyuruCardItem_duyuruTextId)
        var duyuruTarihi: TextView = iv.findViewById(R.id.duyuruCardItem_tarihId)
        var duyuruBtn: RelativeLayout = iv.findViewById(R.id.duyuruCardItem_relativeLayout_duyuruBtn)
    }

}