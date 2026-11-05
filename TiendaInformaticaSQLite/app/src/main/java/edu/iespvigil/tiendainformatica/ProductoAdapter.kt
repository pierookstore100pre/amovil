package edu.iespvigil.tiendainformatica

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.iespvigil.tiendainformatica.databinding.ItemProductoBinding
import java.util.Locale

/**
 * Adaptador del RecyclerView: dibuja una tarjeta (item_producto.xml) por cada producto.
 * Cuando se toca una tarjeta se ejecuta alHacerClic con el producto tocado.
 */
class ProductoAdapter(
    private val alHacerClic: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    private var lista: List<Producto> = emptyList()

    /** Reemplaza la lista mostrada y vuelve a dibujar. */
    fun actualizar(nuevaLista: List<Producto>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

    class ProductoViewHolder(val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductoViewHolder(binding)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = lista[position]
        val b = holder.binding

        b.tvNombre.text = producto.nombre
        b.tvPrecio.text = String.format(Locale.US, "S/ %.2f", producto.precio)

        b.tvCategoriaMarca.text =
            if (producto.marca.isNotBlank()) "${producto.categoria} • ${producto.marca}"
            else producto.categoria

        // Estado de stock con su color (igual que las etiquetas del proyecto web)
        val estado = producto.estadoStock()
        b.tvEstado.text =
            if (producto.activo) estado.texto else "${estado.texto} • Inactivo"
        b.tvEstado.setTextColor(
            when (estado) {
                EstadoStock.DISPONIBLE -> Color.parseColor("#2E7D32")
                EstadoStock.POCAS_UNIDADES -> Color.parseColor("#EF6C00")
                EstadoStock.AGOTADO -> Color.parseColor("#C62828")
                EstadoStock.A_PEDIDO -> Color.parseColor("#1565C0")
            }
        )
        b.tvStock.text = "Stock: ${producto.stock}"

        // Los productos inactivos se ven atenuados
        b.root.alpha = if (producto.activo) 1f else 0.5f

        b.root.setOnClickListener { alHacerClic(producto) }
    }
}
