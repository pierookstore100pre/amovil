package edu.iespvigil.simulacro01

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private val urlBase = "https://apstivigil.edu.pe/2026/disenoweb/nocturno/202644/img/"

    // Categorías que muestra el menú de la página web
    private val categorias = arrayOf(
        "Computadoras: Laptops Gamer",
        "Computadoras: PC Armadas",
        "Computadoras: Todo en Uno",
        "Periféricos: Teclados Mecánicos",
        "Periféricos: Mouses RGB",
        "Periféricos: Audífonos 7.1",
        "Componentes: Procesadores",
        "Componentes: Tarjetas Gráficas",
        "Componentes: Memorias RAM"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Respetar la barra de estado y de navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Activar toolbar personalizado
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        // 2. Recibir el usuario enviado desde el Login
        val usuario = intent.extras?.getString("usuario") ?: "invitado"
        findViewById<TextView>(R.id.tvBienvenida).text = getString(R.string.bienvenido, usuario)

        // 3. Banner principal de la página web
        Glide.with(this)
            .load(urlBase + "2.png")
            .into(findViewById<ImageView>(R.id.amIvBanner))

        // 4. Lista de productos destacados
        val recyclerView: RecyclerView = findViewById(R.id.amRvProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AdaptadorProductos(generarProductos(), this)
    }

    // Productos destacados de https://apstivigil.edu.pe/2026/disenoweb/nocturno/202644/index.php
    private fun generarProductos(): ArrayList<Producto> {
        val lista = ArrayList<Producto>()
        lista.add(Producto(30, "Audífono Logitech G332", "Parlantes PC", 129.99, 36, urlBase + "img4.jpg"))
        lista.add(Producto(29, "Mouse Logitech G203", "Todo en Uno", 99.99, 11, urlBase + "img3.jpg"))
        lista.add(Producto(28, "Asus TUF Gaming F15", "Tarjetas Gráficas", 3199.99, 34, urlBase + "img2.jpg"))
        lista.add(Producto(27, "Lenovo LOQ Gaming", "Tabletas Gráficas", 3099.99, 12, urlBase + "img1.jpg"))
        lista.add(Producto(26, "Parlante BT Wonderwoom red", "Tarjetas Gráficas", 139.99, 31, urlBase + "img13.jpg"))
        lista.add(Producto(25, "Parlante BT Wonderwoom Lila", "Tabletas Gráficas", 139.99, 29, urlBase + "img12.jpg"))
        lista.add(Producto(24, "Web cam Logitech c922e", "Audífonos 7.1", 249.99, 14, urlBase + "img11.jpg"))
        lista.add(Producto(23, "Fuente Poder Atom 550", "Tarjetas Gráficas", 189.99, 25, urlBase + "img10.jpg"))
        return lista
    }

    // 5. Inflar el menú en el toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 6. Manejar cada opción del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_categorias -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.menu_categorias)
                    .setItems(categorias, null)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }

            R.id.action_salir -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
