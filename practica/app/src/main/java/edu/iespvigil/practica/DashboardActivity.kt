package edu.iespvigil.practica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val idEstudiante = intent.getIntExtra("idEstudiante", 0)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val db = DbHelper(this)

        findViewById<TextView>(R.id.tvNombre).text = nombre

        val rv = findViewById<RecyclerView>(R.id.rvTramites)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = TramiteAdapter(db.listarTramites()) { tramite ->
            val intent = Intent(this, DetalleTramiteActivity::class.java)
            intent.putExtra("idTramite", tramite.id)
            intent.putExtra("idEstudiante", idEstudiante)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnPendientes).setOnClickListener {
            val intent = Intent(this, PendientesActivity::class.java)
            intent.putExtra("idEstudiante", idEstudiante)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnSalir).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
