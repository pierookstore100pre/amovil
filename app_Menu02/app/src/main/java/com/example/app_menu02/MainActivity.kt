package com.example.app_menu02

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ⚠️ Esta línea se usa SOLO en la FASE 7 (Toolbar personalizado)
        // Si ya agregaste el <Toolbar> a tu activity_main.xml, descoméntala:
        // setSupportActionBar(findViewById(R.id.my_toolbar))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // ============================================
    // MÉTODO 1: Muestra el menú en el toolbar
    // ============================================
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // ============================================
    // MÉTODO 2: Qué hacer al seleccionar cada opción
    // ============================================
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> Toast.makeText(this, "Guardar", Toast.LENGTH_SHORT).show()
            R.id.action_profile -> Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show()
            R.id.action_exit -> Toast.makeText(this, "Salir", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}
