package com.pierook.ti

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pierook.ti.data.DatabaseHelper

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val db = DatabaseHelper(this)

        val etUser = findViewById<EditText>(R.id.etRegUser)
        val etPass = findViewById<EditText>(R.id.etRegPass)
        val etPass2 = findViewById<EditText>(R.id.etRegPass2)

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val u = etUser.text.toString().trim()
            val p = etPass.text.toString()
            when {
                u.length < 3 -> toast("El usuario debe tener al menos 3 caracteres")
                p.length < 6 -> toast("La contraseña debe tener al menos 6 caracteres")
                p != etPass2.text.toString() -> toast("Las contraseñas no coinciden")
                db.registerUser(u, p) -> { toast("Cuenta creada. Ya puedes ingresar"); finish() }
                else -> toast("Ese usuario ya existe")
            }
        }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
