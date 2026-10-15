package com.example.appplazavea

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ConfiguracionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        // 1. Activar el toolbar personalizado
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 2. Referenciar los switches
        val swNotificaciones = findViewById<Switch>(R.id.swNotificaciones)
        val swModoOscuro = findViewById<Switch>(R.id.swModoOscuro)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        // 3. Evento del switch Notificaciones
        swNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            val mensaje = if (isChecked) "Notificaciones activadas" else "Notificaciones desactivadas"
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }

        // 4. Evento del switch Modo oscuro
        swModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            val mensaje = if (isChecked) "Modo oscuro activado" else "Modo oscuro desactivado"
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }

        // 5. Evento del botón Cerrar sesión
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    // 6. Qué hace la flecha ←
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
