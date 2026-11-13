package edu.iespvigil.practica

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PendientesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendientes)

        val idEstudiante = intent.getIntExtra("idEstudiante", 0)
        val lista = DbHelper(this).listarPendientes(idEstudiante)

        val rv = findViewById<RecyclerView>(R.id.rvPendientes)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = PendienteAdapter(lista)

        findViewById<TextView>(R.id.tvVacio).visibility =
            if (lista.isEmpty()) View.VISIBLE else View.GONE

        findViewById<Button>(R.id.btnVolver).setOnClickListener { finish() }
    }
}
