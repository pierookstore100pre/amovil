package edu.iespvigil.listapeliculas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
class Detalles : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles)

        // 1. Recibimos el objeto Pelicula que enviamos desde el Adaptador
        val pelicula = intent.getSerializableExtra("pel") as Pelicula

        // 2. Buscamos los TextViews en el layout
        val tvTitulo = findViewById<TextView>(R.id.adTvTitulo)
        val tvDirector = findViewById<TextView>(R.id.adTvDirector)
        val tvGenero = findViewById<TextView>(R.id.adTvGenero)
        val tvCalificacion = findViewById<TextView>(R.id.adTvCalificacion)
        val tvDuracion = findViewById<TextView>(R.id.adTvDuracion)
        val tvFecha = findViewById<TextView>(R.id.adTvFecha)

        // 3. Asignamos los textos usando los strings que creamos
        tvTitulo.text = String.format(getString(R.string.titulo), pelicula.titulo)
        tvDirector.text = String.format(getString(R.string.director), pelicula.director)
        tvGenero.text = String.format(getString(R.string.genero), pelicula.genero)
        tvCalificacion.text = String.format(getString(R.string.calificacion), pelicula.calificacion.toString())
        tvDuracion.text = String.format(getString(R.string.duracion), pelicula.duracion.toString())
        tvFecha.text = String.format(getString(R.string.fecha), pelicula.fecha)
    }
}
