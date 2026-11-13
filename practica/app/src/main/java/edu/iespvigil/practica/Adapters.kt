package edu.iespvigil.practica

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TramiteAdapter(
    private val items: List<Tramite>,
    private val onClick: (Tramite) -> Unit
) : RecyclerView.Adapter<TramiteAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val nombre: TextView = v.findViewById(R.id.tvNombre)
        val descripcion: TextView = v.findViewById(R.id.tvDescripcion)
        val costo: TextView = v.findViewById(R.id.tvCosto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_tramite, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val t = items[position]
        holder.nombre.text = t.nombre
        holder.descripcion.text = t.descripcion
        holder.costo.text = "Costo: " + t.costo
        holder.itemView.setOnClickListener { onClick(t) }
    }
}

class PendienteAdapter(private val items: List<Pendiente>) :
    RecyclerView.Adapter<PendienteAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tramite: TextView = v.findViewById(R.id.tvTramite)
        val fecha: TextView = v.findViewById(R.id.tvFecha)
        val estudiante: TextView = v.findViewById(R.id.tvEstudiante)
        val estado: TextView = v.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_pendiente, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = items[position]
        holder.tramite.text = p.tramite
        holder.fecha.text = "Fecha: " + p.fecha
        holder.estudiante.text = "Estudiante: " + p.estudiante
        holder.estado.text = p.estado
    }
}
