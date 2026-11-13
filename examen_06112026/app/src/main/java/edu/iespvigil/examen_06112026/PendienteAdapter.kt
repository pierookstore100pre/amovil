package edu.iespvigil.examen_06112026

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PendienteAdapter(
    private val pendientes: List<TramitePendiente>
) : RecyclerView.Adapter<PendienteAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTramite: TextView = view.findViewById(R.id.tvTramite)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvEstudiante: TextView = view.findViewById(R.id.tvEstudiante)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pendiente, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = pendientes[position]
        val ctx = holder.itemView.context
        holder.tvTramite.text = p.tramite
        holder.tvFecha.text = ctx.getString(R.string.item_fecha, p.fechaInicio)
        holder.tvEstudiante.text = ctx.getString(R.string.item_estudiante, p.estudiante)
    }

    override fun getItemCount() = pendientes.size
}
