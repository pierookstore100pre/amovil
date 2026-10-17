package com.example.entreventanaspasandodatos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResumenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)

        val tvCliente = findViewById<TextView>(R.id.tvCliente)
        val tvPlato = findViewById<TextView>(R.id.tvPlato)
        val tvCantidad = findViewById<TextView>(R.id.tvCantidad)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        val btnFinalizar = findViewById<Button>(R.id.btnFinalizar)

        intent.extras?.let {
            tvCliente.text = it.getString("cliente")
            tvPlato.text = it.getString("plato")
            tvCantidad.text = it.getInt("cantidad").toString()
            tvTotal.text = it.getDouble("total").toString()
        }

        btnRegresar.setOnClickListener {
            finish()
        }

        btnFinalizar.setOnClickListener {
            finishAffinity()
        }
    }
}
