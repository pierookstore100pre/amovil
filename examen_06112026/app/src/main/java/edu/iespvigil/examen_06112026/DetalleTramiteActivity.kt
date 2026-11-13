package edu.iespvigil.examen_06112026

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetalleTramiteActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TRAMITE_ID = "tramite_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_tramite)

        val db = DbHelper(this)
        val tramiteId = intent.getIntExtra(EXTRA_TRAMITE_ID, -1)
        val estudianteId = intent.getIntExtra(DashboardActivity.EXTRA_ESTUDIANTE_ID, -1)

        val tramite = db.obtenerTramite(tramiteId)
        if (tramite == null) {
            finish()
            return
        }

        findViewById<TextView>(R.id.tvNombre).text = tramite.nombre
        findViewById<TextView>(R.id.tvDescripcion).text = tramite.descripcion
        findViewById<TextView>(R.id.tvRequisitos).text = tramite.requisitos
        findViewById<TextView>(R.id.tvCosto).text = getString(R.string.detalle_costo, tramite.costo)

        findViewById<Button>(R.id.btnIniciarTramite).setOnClickListener {
            if (db.existePendiente(estudianteId, tramite.id)) {
                Toast.makeText(this, R.string.tramite_ya_pendiente, Toast.LENGTH_SHORT).show()
            } else {
                db.iniciarTramite(estudianteId, tramite.id)
                Toast.makeText(this, R.string.tramite_iniciado, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
