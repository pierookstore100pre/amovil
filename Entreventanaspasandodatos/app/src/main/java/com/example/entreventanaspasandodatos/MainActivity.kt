package com.example.entreventanaspasandodatos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 1. Referenciamos los componentes del layout
        val etDni = findViewById<EditText>(R.id.etDni)
        val etNombres = findViewById<EditText>(R.id.etNombres)
        val etApellidos = findViewById<EditText>(R.id.etApellidos)
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)

        // 2. Programamos el botón Continuar
        btnContinuar.setOnClickListener {
            // Creamos el Intent para ir a SegundaActivity
            val intent = Intent(this, SegundaActivity::class.java)

            // Enviamos los datos con putExtra
            intent.putExtra("dni", etDni.getText().toString())
            intent.putExtra("nombres", etNombres.getText().toString())
            intent.putExtra("apellidos", etApellidos.getText().toString())

            // Iniciamos la segunda activity
            startActivity(intent)
        }

        // Código de plantilla (para que no quede debajo de la barra de estado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
