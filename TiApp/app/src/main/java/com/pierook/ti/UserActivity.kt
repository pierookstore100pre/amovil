package com.pierook.ti

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pierook.ti.data.DatabaseHelper

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val db = DatabaseHelper(this)

        findViewById<TextView>(R.id.tvWelcome).text =
            "Bienvenido, ${intent.getStringExtra("username")}"

        val labels = db.getProducts().map { "${it.name}\nS/ %.2f  •  stock: ${it.stock}".format(it.price) }
        findViewById<ListView>(R.id.lvProducts).adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, labels)

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
