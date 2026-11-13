package edu.iespvigil.practica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = DbHelper(this)
        val etUsuario = findViewById<TextInputEditText>(R.id.etUsuario)
        val etClave = findViewById<TextInputEditText>(R.id.etClave)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)

        btnIngresar.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val clave = etClave.text.toString()

            if (usuario.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val estudiante = db.login(usuario, clave)
            if (estudiante == null) {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("idEstudiante", estudiante.id)
                intent.putExtra("nombre", estudiante.nombre)
                startActivity(intent)
                finish()
            }
        }
    }
}
