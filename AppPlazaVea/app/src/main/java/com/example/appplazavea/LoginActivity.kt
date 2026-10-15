package com.example.appplazavea

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Referenciamos los componentes del layout
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etClave = findViewById<EditText>(R.id.etClave)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        // 2. Programamos el botón "Entrar"
        btnEntrar.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val clave = etClave.text.toString().trim()

            // 3. Validamos las credenciales
            if (usuario == "Alex" && clave == "Alex12345") {
                // Credenciales correctas → ir a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("usuario", usuario)
                startActivity(intent)
                finish()
            } else {
                // Credenciales incorrectas → mostrar Toast
                Toast.makeText(
                    this,
                    "Usuario o contraseña incorrectos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
