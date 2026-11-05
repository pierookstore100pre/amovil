package edu.iespvigil.tiendainformatica

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.iespvigil.tiendainformatica.databinding.ActivityProductoFormBinding
import java.util.Locale

/**
 * Formulario de producto: sirve para crear uno nuevo o para editar, eliminar
 * y registrar entradas/salidas de stock de uno existente.
 * Si recibe EXTRA_ID es edición; si no, es un producto nuevo.
 */
class ProductoFormActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "producto_id"
    }

    private lateinit var binding: ActivityProductoFormBinding
    private lateinit var tiendaDB: TiendaDbHelper

    private var productoId = 0
    private var categorias: List<Opcion> = emptyList()
    private var marcas: List<Opcion> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductoFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tiendaDB = TiendaDbHelper(this)

        // Un producto necesita categoría; la marca es opcional
        categorias = tiendaDB.listarMaestro(TiendaDbHelper.TABLA_CATEGORIAS)
        if (categorias.isEmpty()) {
            aviso("Primero registra una categoría (menú de la pantalla principal)")
            finish()
            return
        }
        marcas = listOf(Opcion(0, "Sin marca")) +
                tiendaDB.listarMaestro(TiendaDbHelper.TABLA_MARCAS)

        binding.spCategoria.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        binding.spMarca.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, marcas)

        // ¿Es un producto existente o uno nuevo?
        productoId = intent.getIntExtra(EXTRA_ID, 0)
        if (productoId > 0) {
            val producto = tiendaDB.obtenerProducto(productoId)
            if (producto == null) {
                aviso("El producto ya no existe")
                finish()
                return
            }
            mostrarProducto(producto)
        } else {
            binding.tvTitulo.text = "Nuevo producto"
        }

        binding.btGuardar.setOnClickListener { guardar() }
        binding.btEliminar.setOnClickListener { confirmarEliminar() }
        binding.btEntrada.setOnClickListener { ajustarStock(esEntrada = true) }
        binding.btSalida.setOnClickListener { ajustarStock(esEntrada = false) }
    }

    // Rellena el formulario con los datos del producto que se va a editar
    private fun mostrarProducto(p: Producto) {
        binding.tvTitulo.text = "Editar producto"
        binding.tvActualizado.text = "Última actualización: ${p.actualizado}"
        binding.tvActualizado.visibility = View.VISIBLE

        binding.etNombre.setText(p.nombre)
        binding.etDescripcion.setText(p.descripcion)
        binding.etDescripcionLarga.setText(p.descripcionLarga)
        binding.etPrecio.setText(String.format(Locale.US, "%.2f", p.precio))

        // Al editar, el stock solo cambia con Entrada / Salida
        binding.etStock.setText(p.stock.toString())
        binding.etStock.isEnabled = false
        binding.tvAyudaStock.visibility = View.VISIBLE
        binding.llStock.visibility = View.VISIBLE

        binding.spCategoria.setSelection(
            categorias.indexOfFirst { it.id == p.categoriaId }.coerceAtLeast(0)
        )
        binding.spMarca.setSelection(
            marcas.indexOfFirst { it.id == p.marcaId }.coerceAtLeast(0)
        )
        binding.swAPedido.isChecked = p.aPedido
        binding.swActivo.isChecked = p.activo

        binding.btEliminar.visibility = View.VISIBLE
    }

    // Botón Guardar: valida los datos y añade o modifica el producto
    private fun guardar() {
        val nombre = binding.etNombre.text.toString().trim()
        val precio = binding.etPrecio.text.toString().trim().toDoubleOrNull()
        val stock = binding.etStock.text.toString().trim().toIntOrNull() ?: 0

        if (nombre.isEmpty() || precio == null) {
            aviso("Escribe el nombre y un precio válido")
            return
        }

        val categoria = binding.spCategoria.selectedItem as Opcion
        val marca = binding.spMarca.selectedItem as Opcion

        val producto = Producto(
            id = productoId,
            categoriaId = categoria.id,
            marcaId = marca.id,
            nombre = nombre,
            descripcion = binding.etDescripcion.text.toString().trim(),
            descripcionLarga = binding.etDescripcionLarga.text.toString().trim(),
            precio = precio,
            stock = stock,
            aPedido = binding.swAPedido.isChecked,
            activo = binding.swActivo.isChecked
        )

        if (productoId == 0) {
            tiendaDB.anadirProducto(producto)
            aviso("Guardado")
        } else {
            tiendaDB.modificarProducto(producto)
            aviso("Modificado")
        }
        finish()
    }

    // Botones Entrada / Salida: suman o restan stock directamente en la base de datos
    private fun ajustarStock(esEntrada: Boolean) {
        val cantidad = binding.etCantidad.text.toString().trim().toIntOrNull() ?: 0
        if (cantidad < 1) {
            aviso("Escribe una cantidad mayor que 0")
            return
        }

        val nuevoStock = tiendaDB.ajustarStock(productoId, cantidad, esEntrada)
        if (nuevoStock < 0) {
            aviso("La salida supera el stock disponible")
        } else {
            binding.etStock.setText(nuevoStock.toString())
            binding.etCantidad.text.clear()
            aviso("Stock actualizado: $nuevoStock")
        }
    }

    // Botón Eliminar: pide confirmación antes de borrar
    private fun confirmarEliminar() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar producto")
            .setMessage("¿Seguro que deseas eliminar \"${binding.etNombre.text}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                val cantidad = tiendaDB.borrarProducto(productoId)
                aviso("Datos borrados: $cantidad")
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun aviso(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
