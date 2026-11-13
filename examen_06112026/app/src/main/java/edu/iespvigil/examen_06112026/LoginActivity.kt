package edu.iespvigil.examen_06112026

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var db: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = DbHelper(this)

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)

        btnIngresar.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val contrasena = etContrasena.text.toString()

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, R.string.error_vacio, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val estudiante = db.login(usuario, contrasena)
            if (estudiante == null) {
                Toast.makeText(this, R.string.error_credenciales, Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, DashboardActivity::class.java).apply {
                    putExtra(DashboardActivity.EXTRA_ESTUDIANTE_ID, estudiante.id)
                    putExtra(DashboardActivity.EXTRA_ESTUDIANTE_NOMBRE, estudiante.nombreCompleto)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}
