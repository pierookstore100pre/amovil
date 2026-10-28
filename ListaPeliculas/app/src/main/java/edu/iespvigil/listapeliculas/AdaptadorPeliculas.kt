package edu.iespvigil.listapeliculas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorPeliculas(private var lista: ArrayList<Pelicula>, private var contexto: Context): RecyclerView.Adapter<AdaptadorPeliculas.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.elemento_lista_pelicula, parent, false)
        return ViewHolder(vista, contexto)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    class ViewHolder(var vista: View, var contexto: Context) : RecyclerView.ViewHolder(vista) {
        fun bind(pelicula: Pelicula) {
            vista.findViewById<ImageView>(R.id.elpIvPelicula).setImageResource(pelicula.idImagen)
            vista.findViewById<TextView>(R.id.elpTvTitulo).text = pelicula.titulo
            vista.findViewById<TextView>(R.id.elpTvGenero).text = pelicula.genero
            vista.findViewById<RatingBar>(R.id.elpRbCalificacion).rating = pelicula.calificacion.toFloat()

            // Clic en la Imagen (Ir a VisorImagen)
            vista.findViewById<ImageView>(R.id.elpIvPelicula).setOnClickListener {
                val intent = Intent(contexto, VisorImagen::class.java)
                intent.putExtra("pel", pelicula)
                contexto.startActivity(intent)
            }

            // Clic en el elemento completo (Ir a Detalles)
            vista.setOnClickListener {
                val intent = Intent(contexto, Detalles::class.java)
                intent.putExtra("pel", pelicula)
                contexto.startActivity(intent)
            }
        }
    }
}
