package com.example.entreventanaspasandodatos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SegundaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_segunda)

        // 1. Referenciamos los TextView donde mostraremos los datos
        val tvDniValor = findViewById<TextView>(R.id.tvDniValor)
        val tvNombresValor = findViewById<TextView>(R.id.tvNombresValor)
        val tvApellidosValor = findViewById<TextView>(R.id.tvApellidosValor)

        // 2. Recibimos los datos del Intent
        val dni = intent.extras?.getString("dni") ?: ""
        val nombres = intent.extras?.getString("nombres") ?: ""
        val apellidos = intent.extras?.getString("apellidos") ?: ""

        // 3. Mostramos los datos en los TextView
        tvDniValor.text = dni
        tvNombresValor.text = nombres
        tvApellidosValor.text = apellidos

        // 4. Botón REGRESAR: vuelve a la pantalla anterior
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            finish()
        }

        // 5. Botón FINALIZAR: cierra toda la app
        val btnFinalizar = findViewById<Button>(R.id.btnFinalizar)
        btnFinalizar.setOnClickListener {
            finishAffinity()
        }

        // Código de plantilla (para que no quede debajo de la barra de estado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}