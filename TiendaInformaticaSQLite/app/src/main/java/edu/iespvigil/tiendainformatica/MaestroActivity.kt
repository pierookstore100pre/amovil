package edu.iespvigil.tiendainformatica

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.iespvigil.tiendainformatica.databinding.ActivityMaestroBinding

/**
 * Mantenimiento de categorías o marcas (la misma pantalla sirve para las dos tablas).
 * Recibe en EXTRA_TABLA el nombre de la tabla a mantener.
 * Funciona como la guía 03_02: Guardar, Modificar (tocando un elemento), Borrar y Consultar.
 */
class MaestroActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TABLA = "tabla"
    }

    private lateinit var binding: ActivityMaestroBinding
    private lateinit var tiendaDB: TiendaDbHelper

    private var tabla = TiendaDbHelper.TABLA_CATEGORIAS
    private var items: List<Opcion> = emptyList()
    private var seleccionadoId = 0   // 0 = ningún elemento seleccionado (se creará uno nuevo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMaestroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tiendaDB = TiendaDbHelper(this)

        tabla = intent.getStringExtra(EXTRA_TABLA) ?: TiendaDbHelper.TABLA_CATEGORIAS
        binding.tvTitulo.text =
            if (tabla == TiendaDbHelper.TABLA_MARCAS) "Marcas" else "Categorías"

        // Al tocar un elemento de la lista, se carga su nombre para modificarlo o borrarlo
        binding.lvItems.setOnItemClickListener { _, _, posicion, _ ->
            val item = items[posicion]
            seleccionadoId = item.id
            binding.etNombre.setText(item.texto)
        }

        binding.btGuardar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            if (nombre.isEmpty()) {
                aviso("Escribe un nombre")
            } else {
                tiendaDB.guardarMaestro(tabla, seleccionadoId, nombre)
                aviso(if (seleccionadoId == 0) "Guardado" else "Modificado")
                limpiar()
                consultar()
            }
        }

        binding.btEliminar.setOnClickListener {
            if (seleccionadoId == 0) {
                aviso("Toca un elemento de la lista para eliminarlo")
            } else {
                val resultado = tiendaDB.borrarMaestro(tabla, seleccionadoId)
                aviso(
                    when (resultado) {
                        1 -> "Datos borrados: 1"
                        -1 -> "No se puede borrar: hay productos que lo usan"
                        else -> "No se encontró el registro"
                    }
                )
                limpiar()
                consultar()
            }
        }

        binding.btNuevo.setOnClickListener { limpiar() }

        consultar()
    }

    // Lee la tabla y muestra los nombres en el ListView
    private fun consultar() {
        items = tiendaDB.listarMaestro(tabla)
        binding.lvItems.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
    }

    // Deja el formulario listo para un registro nuevo
    private fun limpiar() {
        seleccionadoId = 0
        binding.etNombre.text.clear()
    }

    private fun aviso(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
