package com.example.kneecheck.pasien

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kneecheck.R
import com.example.kneecheck.entity.HistoryPasienData

class HistoryPasienAdapter(
    val data: List<HistoryPasienData>,
    var onAllClickListener: ((HistoryPasienData) -> Unit)? = null
): RecyclerView.Adapter<HistoryPasienAdapter.ViewHolder>(){
    class ViewHolder(val row:View): RecyclerView.ViewHolder(row) {
        val xray: ImageView = row.findViewById(R.id.rvImageXray)
        val tvTanggal: TextView = row.findViewById(R.id.rvTvTanggal)
        val tvNama: TextView = row.findViewById(R.id.rvTvNama)
        val tvSeverity: TextView = row.findViewById(R.id.rvTvSeverity)
        var views = row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.recycle_history, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = data[position]

        holder.tvTanggal.text = history.tgl_scan
        holder.tvNama.text = history.name
        holder.tvSeverity.text = "Tingkat keparahan : " + history.confidence_score
        when(history.confidence_score){
            4 -> holder.tvSeverity.setTextColor(Color.RED)
            2 -> holder.tvSeverity.setTextColor(Color.BLUE)
            0 -> holder.tvSeverity.setTextColor(Color.GREEN)
        }

        Glide.with(holder.views)
            .load(history.img)
            .apply(RequestOptions().placeholder(R.drawable.logo))
            .into(holder.xray)

        holder.views.setOnClickListener {
            onAllClickListener?.invoke(history)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}