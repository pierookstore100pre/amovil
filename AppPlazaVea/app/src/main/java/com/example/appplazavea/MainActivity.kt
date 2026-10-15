package com.example.appplazavea

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Activar toolbar personalizado
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        // 2. Recibir el usuario enviado desde el Login
        val usuario = intent.extras?.getString("usuario") ?: "invitado"

        // 3. Mostrar el mensaje de bienvenida
        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        tvBienvenida.text = "Bienvenido, $usuario"
    }

    // 4. Inflar el menú en el toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 5. Manejar cada opción del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_mi_cuenta ->
                startActivity(Intent(this, MiCuentaActivity::class.java))

            R.id.action_mis_pedidos ->
                startActivity(Intent(this, MisPedidosActivity::class.java))

            R.id.action_mis_pagos ->
                startActivity(Intent(this, MisPagosActivity::class.java))

            R.id.action_configuracion ->
                startActivity(Intent(this, ConfiguracionActivity::class.java))

            R.id.action_salir -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
