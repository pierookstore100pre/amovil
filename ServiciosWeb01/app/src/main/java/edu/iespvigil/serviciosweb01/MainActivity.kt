package edu.iespvigil.serviciosweb01

import android.app.DownloadManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        val queue = Volley.newRequestQueue(this)

        val url = "https://6af5193cf6270fa08d67b427.mockapi.io/api/v1/usuarios"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->

            //aqui programamos la respuesta, es decir, cuando reciba los datos

            try {
                val jsonArray = JSONArray(response)

                // Recorremos todo el arreglo JSON con un for
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)

                    // Extraemos el campo "nombres" y lo mostramos con un Toast
                    val nombre = jsonObject.getString("nombres")

                    Toast.makeText(applicationContext, "Usuario: $nombre", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                tvResultado.text = "Error al parsear: ${e.message}"
            }

        }, { error ->

            //aqui programamos cuando se encuentre un error

        })

        queue.add(stringRequest)
    }
}
