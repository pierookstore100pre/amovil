package com.example.sqlite01

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlite01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var amigosDBHelper: miSQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Código de plantilla (para que no quede debajo de la barra de estado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Variable que permite cargar la base de datos
        amigosDBHelper = miSQLiteHelper(this)

        // Botón Guardar
        binding.btGuardar.setOnClickListener {
            if (binding.etNombre.text.isNotBlank() && binding.etEmail.text.isNotBlank()) {

                amigosDBHelper.anadirDato(
                    binding.etNombre.text.toString(),
                    binding.etEmail.text.toString()
                )

                binding.etNombre.text.clear()
                binding.etEmail.text.clear()

                Toast.makeText(this, "Guardado", Toast.LENGTH_LONG).show()
                binding.btConsultar.callOnClick()

            } else {
                Toast.makeText(this, "No se ha podido guardar", Toast.LENGTH_LONG).show()
            }
        }

        // Botón Consultar
        binding.btConsultar.setOnClickListener {
            binding.tvConsulta.text = ""
            val db: SQLiteDatabase = amigosDBHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM amigos", null)

            if (cursor.moveToFirst()) {
                do {
                    binding.tvConsulta.append(cursor.getInt(0).toString() + ":")
                    binding.tvConsulta.append(cursor.getString(1).toString() + " ---> ")
                    binding.tvConsulta.append(cursor.getString(2).toString() + "\n")
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
    }
}
