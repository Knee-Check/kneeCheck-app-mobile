package com.example.kneecheck.dokter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kneecheck.R
import com.example.kneecheck.entity.pasienData
import org.w3c.dom.Text

class HasilPencarianAdapter(
    val data: List<pasienData>,
    var onAllClickListener: ((pasienData) -> Unit)? = null
): RecyclerView.Adapter<HasilPencarianAdapter.ViewHolder>() {
    class ViewHolder(val row: View): RecyclerView.ViewHolder(row) {
        val tvNama: TextView = row.findViewById(R.id.rvNamaHasilCari)
        val tvEmail: TextView = row.findViewById(R.id.rvEmailHasilCari)
        val tvBirth: TextView = row.findViewById(R.id.rvBirthHasilCari)
        val tvKota: TextView = row.findViewById(R.id.rvKotaHasilCari)
        var views = row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.recycle_hasil_pencarian, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hasilcari = data[position]
        holder.tvNama.text = hasilcari.name
        holder.tvEmail.text = hasilcari.email
        holder.tvBirth.text = hasilcari.birth.substring(0, 10)
        holder.tvKota.text = hasilcari.address

        holder.views.setOnClickListener {
            onAllClickListener?.invoke(hasilcari)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}