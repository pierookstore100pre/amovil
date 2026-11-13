package edu.iespvigil.practica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetalleTramiteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val idTramite = intent.getIntExtra("idTramite", 0)
        val idEstudiante = intent.getIntExtra("idEstudiante", 0)
        val db = DbHelper(this)

        val tramite = db.obtenerTramite(idTramite)
        if (tramite == null) {
            finish()
            return
        }

        findViewById<TextView>(R.id.tvTitulo).text = tramite.nombre
        findViewById<TextView>(R.id.tvDescripcion).text = tramite.descripcion
        findViewById<TextView>(R.id.tvRequisitos).text = tramite.requisitos
        findViewById<TextView>(R.id.tvCosto).text = tramite.costo

        findViewById<Button>(R.id.btnIniciar).setOnClickListener {
            if (db.iniciarTramite(idEstudiante, idTramite)) {
                Toast.makeText(this, "Trámite iniciado correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PendientesActivity::class.java)
                intent.putExtra("idEstudiante", idEstudiante)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "No se pudo iniciar el trámite", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnVolver).setOnClickListener { finish() }
    }
}
