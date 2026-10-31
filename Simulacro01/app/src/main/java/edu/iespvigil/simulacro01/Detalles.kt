package edu.iespvigil.simulacro01

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.util.Locale

class Detalles : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles)

        // 1. Recibimos el objeto Producto que enviamos desde el Adaptador
        val producto = intent.getSerializableExtra("pro") as Producto

        // 2. Buscamos los componentes en el layout
        val ivProducto = findViewById<ImageView>(R.id.adIvProducto)
        val tvNombre = findViewById<TextView>(R.id.adTvNombre)
        val tvCodigo = findViewById<TextView>(R.id.adTvCodigo)
        val tvCategoria = findViewById<TextView>(R.id.adTvCategoria)
        val tvPrecio = findViewById<TextView>(R.id.adTvPrecio)
        val tvStock = findViewById<TextView>(R.id.adTvStock)

        // 3. Asignamos los valores
        Glide.with(this)
            .load(producto.imagenUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(ivProducto)

        tvNombre.text = String.format(getString(R.string.nombre), producto.nombre)
        tvCodigo.text = String.format(getString(R.string.codigo), producto.id.toString())
        tvCategoria.text = String.format(getString(R.string.categoria), producto.categoria)
        tvPrecio.text = String.format(
            getString(R.string.precio),
            String.format(Locale.US, "S/ %,.2f", producto.precio)
        )
        tvStock.text = String.format(getString(R.string.stock), producto.stock.toString())
    }
}
