package com.example.entreventanaspasandodatos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class PedidoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)

        val etCliente = findViewById<EditText>(R.id.etCliente)
        val etPlato = findViewById<EditText>(R.id.etPlato)
        val etCantidad = findViewById<EditText>(R.id.etCantidad)
        val etPrecio = findViewById<EditText>(R.id.etPrecio)
        val btnPedir = findViewById<Button>(R.id.btnPedir)

        btnPedir.setOnClickListener {
            val cliente = etCliente.text.toString()
            val plato = etPlato.text.toString()
            val cantidad = etCantidad.text.toString().toIntOrNull() ?: 0
            val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val total = cantidad * precio

            val intent = Intent(this, ResumenActivity::class.java)
            intent.putExtra("cliente", cliente)
            intent.putExtra("plato", plato)
            intent.putExtra("cantidad", cantidad)
            intent.putExtra("total", total)
            startActivity(intent)
        }
    }
}
