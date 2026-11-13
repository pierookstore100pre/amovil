package edu.iespvigil.examen_06112026

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TramiteAdapter(
    private val tramites: List<Tramite>,
    private val onClick: (Tramite) -> Unit
) : RecyclerView.Adapter<TramiteAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_tramite, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val t = tramites[position]
        holder.tvNombre.text = t.nombre
        holder.tvDescripcion.text = t.descripcion
        holder.itemView.setOnClickListener { onClick(t) }
    }

    override fun getItemCount() = tramites.size
}
