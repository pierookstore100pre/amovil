package edu.iespvigil.listapeliculas

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VisorImagen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_visor_imagen)

        // 1. Recibimos la película
        val pelicula = intent.getSerializableExtra("pel") as Pelicula

        // 2. Buscamos la imagen y le asignamos el recurso
        val cPelicula = findViewById<ImageView>(R.id.avilvPelicula)
        cPelicula.setImageResource(pelicula.idImagen)

        // 3. El código de los márgenes de la pantalla (Edge to Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
