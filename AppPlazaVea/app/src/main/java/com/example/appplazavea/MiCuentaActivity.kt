package com.example.appplazavea

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MiCuentaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_cuenta)

        // 1. Activar el toolbar personalizado
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        // 2. Mostrar la flecha ← para regresar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // 3. Qué hace la flecha ←
    override fun onSupportNavigateUp(): Boolean {
        finish() // Cierra esta pantalla y vuelve a MainActivity
        return true
    }
}
