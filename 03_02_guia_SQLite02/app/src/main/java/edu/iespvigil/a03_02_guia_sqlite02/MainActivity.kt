package edu.iespvigil.a03_02_guia_sqlite02

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.iespvigil.a03_02_guia_sqlite02.databinding.ActivityMainBinding

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

        // Cuando se haga clic sobre el botón Guardar
        binding.btGuardar.setOnClickListener {
            // Se analiza que los campos nombre e email no estén en blanco
            if (binding.etNombre.text.isNotBlank() && binding.etEmail.text.isNotBlank()) {

                // Llamo a la función añadir dato y le envío nombre e email
                amigosDBHelper.anadirDato(
                    binding.etNombre.text.toString(),
                    binding.etEmail.text.toString()
                )

                // Borro el contenido de los campos
                binding.etNombre.text.clear()
                binding.etEmail.text.clear()

                Toast.makeText(this, "Guardado", Toast.LENGTH_LONG).show()

                // Se simula el clic sobre el botón Consultar
                binding.btConsultar.callOnClick()

            } else {
                Toast.makeText(this, "No se ha podido guardar", Toast.LENGTH_LONG).show()
            }
        }

        // Cuando se haga clic sobre el botón Consultar
        binding.btConsultar.setOnClickListener {
            // Ponemos en blanco el TextView donde se escriben los datos
            binding.tvConsulta.text = ""

            // Enlace con la base de datos en modo lectura
            val db: SQLiteDatabase = amigosDBHelper.readableDatabase

            // Cursor que almacena los resultados de la consulta
            val cursor = db.rawQuery("SELECT * FROM amigos", null)

            // Si podemos movernos al principio del cursor, hay datos para mostrar
            if (cursor.moveToFirst()) {
                do {
                    // ID (primer campo)
                    binding.tvConsulta.append(cursor.getInt(0).toString() + ":")
                    // Nombre (segundo campo)
                    binding.tvConsulta.append(cursor.getString(1).toString() + " ---> ")
                    // Email (tercer campo)
                    binding.tvConsulta.append(cursor.getString(2).toString() + "\n")
                } while (cursor.moveToNext()) // termina cuando no hay siguiente fila
            }
            cursor.close()
        }

        // Cuando se presione el botón Modificar
        binding.btModificar.setOnClickListener {
            // Si los campos id, nombre e email no están en blanco
            if (binding.etId.text.isNotBlank() && binding.etNombre.text.isNotBlank() &&
                binding.etEmail.text.isNotBlank()
            ) {

                // Se llama a la función modificar con id, nombre e email
                amigosDBHelper.modificarDato(
                    binding.etId.text.toString().toInt(),
                    binding.etNombre.text.toString(),
                    binding.etEmail.text.toString()
                )

                // Se limpian los campos
                binding.etId.text.clear()
                binding.etNombre.text.clear()
                binding.etEmail.text.clear()

                Toast.makeText(this, "Modificado", Toast.LENGTH_LONG).show()

                // Se simula el clic sobre el botón Consultar
                binding.btConsultar.callOnClick()

            } else {
                Toast.makeText(this, "No se ha podido modificar", Toast.LENGTH_LONG).show()
            }
        }

        // Cuando se haga clic en el botón Borrar
        binding.btBorrar.setOnClickListener {
            // Variable para contabilizar cuántos registros se borraron
            var cantidad = 0

            // Si el campo id no está vacío
            if (binding.etId.text.isNotBlank()) {

                // La función devuelve el número de registros borrados
                cantidad = amigosDBHelper.borrarDato(binding.etId.text.toString().toInt())

                // Limpiamos el campo id
                binding.etId.text.clear()

                Toast.makeText(this, "Datos borrados: $cantidad", Toast.LENGTH_LONG).show()

                // Se simula el clic sobre el botón Consultar
                binding.btConsultar.callOnClick()
            }
        }
    }
}
