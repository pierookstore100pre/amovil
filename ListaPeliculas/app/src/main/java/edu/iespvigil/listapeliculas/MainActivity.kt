package edu.iespvigil.listapeliculas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler_view: RecyclerView = findViewById(R.id.amRvPeliculas)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = AdaptadorPeliculas(generarDatosPrueba(), this)
    }

    private fun generarDatosPrueba(): ArrayList<Pelicula> {
        var lista = ArrayList<Pelicula>()
        // Asegúrate de que los nombres R.drawable.xxx coincidan con tus imágenes
        lista.add(Pelicula(R.drawable.ex_maquina, "La Máquina", "Christopher Nolan", "Ciencia ficción", 4.3, 169, "2014"))
        lista.add(Pelicula(R.drawable.extraordinario, "Wonder", "Guillermo del Toro", "Ciencia ficción", 3.65, 123, "2015"))
        lista.add(Pelicula(R.drawable.forma_agua, "The Shape of Water", "Stephen Chbosky", "Drama", 5.0, 113, "2017"))
        lista.add(Pelicula(R.drawable.interestelar, "Inter Stellar", "Denis Villeneuve", "Ciencia ficción", 3.95, 116, "2021"))
        lista.add(Pelicula(R.drawable.jumanji, "Jumanji", "Alex Garland", "Ciencia ficción", 3.85, 108, "2020"))
        lista.add(Pelicula(R.drawable.la_llegada, "Arrival", "Lake Kasdan", "Acción", 3.5, 119, "2017"))

        return lista
    }
}
