package edu.iespvigil.simulacro01

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.Locale

class AdaptadorProductos(private var lista: ArrayList<Producto>, private var contexto: Context) :
    RecyclerView.Adapter<AdaptadorProductos.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.elemento_lista_producto, parent, false)
        return ViewHolder(vista, contexto)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    class ViewHolder(var vista: View, var contexto: Context) : RecyclerView.ViewHolder(vista) {
        fun bind(producto: Producto) {
            val ivProducto = vista.findViewById<ImageView>(R.id.elpIvProducto)

            // La imagen se descarga desde la página web
            Glide.with(contexto)
                .load(producto.imagenUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivProducto)

            vista.findViewById<TextView>(R.id.elpTvNombre).text = producto.nombre
            vista.findViewById<TextView>(R.id.elpTvCategoria).text = producto.categoria
            vista.findViewById<TextView>(R.id.elpTvPrecio).text =
                String.format(Locale.US, "S/ %,.2f", producto.precio)
            vista.findViewById<TextView>(R.id.elpTvStock).text =
                contexto.getString(R.string.stock_corto, producto.stock)

            // Clic en la imagen (ir a VisorImagen)
            ivProducto.setOnClickListener {
                val intent = Intent(contexto, VisorImagen::class.java)
                intent.putExtra("pro", producto)
                contexto.startActivity(intent)
            }

            // Clic en el elemento completo (ir a Detalles)
            vista.setOnClickListener {
                val intent = Intent(contexto, Detalles::class.java)
                intent.putExtra("pro", producto)
                contexto.startActivity(intent)
            }
        }
    }
}
