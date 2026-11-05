package com.pierook.ti

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pierook.ti.data.DatabaseHelper

class AdminActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var lv: ListView
    private lateinit var btnAdd: Button
    private var showingUsers = true
    private var ids: List<Long> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        db = DatabaseHelper(this)

        findViewById<TextView>(R.id.tvTitle).text =
            "Panel de administrador\n${intent.getStringExtra("username")}"
        lv = findViewById(R.id.lvData)
        btnAdd = findViewById(R.id.btnAdd)

        findViewById<Button>(R.id.btnUsers).setOnClickListener { showingUsers = true; refresh() }
        findViewById<Button>(R.id.btnProducts).setOnClickListener { showingUsers = false; refresh() }
        btnAdd.setOnClickListener { showAddProductDialog() }
        findViewById<Button>(R.id.btnLogout).setOnClickListener { logout() }

        lv.setOnItemLongClickListener { _, _, position, _ ->
            confirmDelete(ids[position])
            true
        }
        refresh()
    }

    private fun refresh() {
        val labels: List<String>
        if (showingUsers) {
            val users = db.getUsers()
            ids = users.map { it.id }
            labels = users.map { "${it.username}  [${it.role}]" }
            btnAdd.visibility = View.GONE
        } else {
            val products = db.getProducts()
            ids = products.map { it.id }
            labels = products.map { "${it.name}\nS/ %.2f  •  stock: ${it.stock}".format(it.price) }
            btnAdd.visibility = View.VISIBLE
        }
        lv.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, labels)
    }

    private fun confirmDelete(id: Long) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar este registro?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                val ok = if (showingUsers) db.deleteUser(id) else db.deleteProduct(id)
                if (!ok) Toast.makeText(this, "No se puede eliminar un administrador", Toast.LENGTH_SHORT).show()
                refresh()
            }.show()
    }

    private fun showAddProductDialog() {
        val pad = (16 * resources.displayMetrics.density).toInt()
        val etName = EditText(this).apply { hint = "Nombre" }
        val etPrice = EditText(this).apply {
            hint = "Precio"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        val etStock = EditText(this).apply {
            hint = "Stock"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, 0)
            addView(etName); addView(etPrice); addView(etStock)
        }
        AlertDialog.Builder(this)
            .setTitle("Nuevo producto")
            .setView(box)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar") { _, _ ->
                val name = etName.text.toString().trim()
                val price = etPrice.text.toString().toDoubleOrNull()
                val stock = etStock.text.toString().toIntOrNull()
                if (name.isEmpty() || price == null || stock == null) {
                    Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
                } else {
                    db.addProduct(name, price, stock)
                    refresh()
                }
            }.show()
    }

    private fun logout() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
