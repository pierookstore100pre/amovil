package edu.iespvigil.tiendainformatica

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import edu.iespvigil.tiendainformatica.databinding.ActivityMainBinding

/**
 * Pantalla principal: catálogo de productos con búsqueda y filtros
 * (equivale a la página /catalogo del proyecto web).
 */
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var tiendaDB: TiendaDbHelper
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Código de plantilla (para que no quede debajo de la barra de estado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // La barra superior hace de ActionBar (aquí se muestra el menú)
        setSupportActionBar(binding.toolbar)

        // Variable que permite cargar la base de datos
        tiendaDB = TiendaDbHelper(this)

        // Lista de productos: al tocar una tarjeta se abre el formulario para editarla
        adapter = ProductoAdapter { producto ->
            val intent = Intent(this, ProductoFormActivity::class.java)
            intent.putExtra(ProductoFormActivity.EXTRA_ID, producto.id)
            startActivity(intent)
        }
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = adapter

        // Cada vez que cambia el texto de búsqueda se vuelve a consultar
        binding.etBuscar.doAfterTextChanged { consultar() }

        // Cada vez que se elige otra categoría o marca se vuelve a consultar
        val alCambiarFiltro = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                consultar()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.spCategoria.onItemSelectedListener = alCambiarFiltro
        binding.spMarca.onItemSelectedListener = alCambiarFiltro

        // Botón "+" : abre el formulario vacío para un producto nuevo
        binding.fabNuevo.setOnClickListener {
            startActivity(Intent(this, ProductoFormActivity::class.java))
        }
    }

    // Al volver de otra pantalla se refrescan los filtros y la lista
    override fun onResume() {
        super.onResume()
        cargarFiltros()
        consultar()
    }

    // Llena los Spinner de categoría y marca, conservando lo que estaba elegido
    private fun cargarFiltros() {
        val categoriaElegida = (binding.spCategoria.selectedItem as? Opcion)?.id ?: 0
        val marcaElegida = (binding.spMarca.selectedItem as? Opcion)?.id ?: 0

        val categorias = listOf(Opcion(0, "Todas las categorías")) +
                tiendaDB.listarMaestro(TiendaDbHelper.TABLA_CATEGORIAS)
        val marcas = listOf(Opcion(0, "Todas las marcas")) +
                tiendaDB.listarMaestro(TiendaDbHelper.TABLA_MARCAS)

        binding.spCategoria.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        binding.spMarca.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, marcas)

        binding.spCategoria.setSelection(
            categorias.indexOfFirst { it.id == categoriaElegida }.coerceAtLeast(0)
        )
        binding.spMarca.setSelection(
            marcas.indexOfFirst { it.id == marcaElegida }.coerceAtLeast(0)
        )
    }

    // Consulta los productos según el texto y los filtros, y los muestra en la lista
    private fun consultar() {
        val texto = binding.etBuscar.text.toString().trim()
        val categoriaId = (binding.spCategoria.selectedItem as? Opcion)?.id ?: 0
        val marcaId = (binding.spMarca.selectedItem as? Opcion)?.id ?: 0

        val productos = tiendaDB.listarProductos(texto, categoriaId, marcaId)
        adapter.actualizar(productos)

        val criticos = productos.count { it.esStockCritico() }
        binding.tvResumen.text = "${productos.size} producto(s)  •  $criticos con stock crítico"
        binding.tvVacio.visibility = if (productos.isEmpty()) View.VISIBLE else View.GONE
    }

    // Menú de la barra superior: Categorías y Marcas
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val tabla = when (item.itemId) {
            R.id.menu_categorias -> TiendaDbHelper.TABLA_CATEGORIAS
            R.id.menu_marcas -> TiendaDbHelper.TABLA_MARCAS
            else -> return super.onOptionsItemSelected(item)
        }
        val intent = Intent(this, MaestroActivity::class.java)
        intent.putExtra(MaestroActivity.EXTRA_TABLA, tabla)
        startActivity(intent)
        return true
    }
}
