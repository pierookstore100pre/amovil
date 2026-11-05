package com.pierook.ti

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pierook.ti.data.DatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        db = DatabaseHelper(this)

        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val rbAdmin = findViewById<RadioButton>(R.id.rbAdmin)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val username = etUser.text.toString().trim()
            val password = etPass.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val role = if (rbAdmin.isChecked) DatabaseHelper.ROLE_ADMIN else DatabaseHelper.ROLE_USER
            val user = db.login(username, password, role)
            if (user == null) {
                Toast.makeText(this, "Credenciales o rol incorrectos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val target = if (role == DatabaseHelper.ROLE_ADMIN) AdminActivity::class.java
                         else UserActivity::class.java
            startActivity(Intent(this, target).putExtra("username", user.username))
            finish()
        }

        findViewById<TextView>(R.id.tvRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
