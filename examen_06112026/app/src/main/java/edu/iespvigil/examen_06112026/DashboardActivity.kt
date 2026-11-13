package edu.iespvigil.examen_06112026

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashboardActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ESTUDIANTE_ID = "estudiante_id"
        const val EXTRA_ESTUDIANTE_NOMBRE = "estudiante_nombre"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val estudianteId = intent.getIntExtra(EXTRA_ESTUDIANTE_ID, -1)
        val estudianteNombre = intent.getStringExtra(EXTRA_ESTUDIANTE_NOMBRE) ?: ""

        findViewById<TextView>(R.id.tvBienvenida).text =
            getString(R.string.dashboard_bienvenido, estudianteNombre)

        val tramites = DbHelper(this).listarTramites()

        val rv = findViewById<RecyclerView>(R.id.rvTramites)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = TramiteAdapter(tramites) { tramite ->
            val intent = Intent(this, DetalleTramiteActivity::class.java).apply {
                putExtra(DetalleTramiteActivity.EXTRA_TRAMITE_ID, tramite.id)
                putExtra(EXTRA_ESTUDIANTE_ID, estudianteId)
                putExtra(EXTRA_ESTUDIANTE_NOMBRE, estudianteNombre)
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnPendientes).setOnClickListener {
            startActivity(Intent(this, PendientesActivity::class.java))
        }

        findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
