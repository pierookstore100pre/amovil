package com.example.ciclo_de_vida // Asegúrate de que este sea tu paquete

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // El método onCreate se ejecuta una sola vez, al crear la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Registro para onCreate
        Log.d("CicloVida", "Ejecutando desde el método OnCreate")
    }

    // onStart se llama cuando la actividad se hace visible.
    override fun onStart() {
        super.onStart()
        Log.d("CicloVida", "Ejecutando desde el método OnStart")
    }

    // onResume se llama cuando la actividad está en primer plano y lista para interactuar.
    override fun onResume() {
        super.onResume()
        Log.d("CicloVida", "Ejecutando desde el método onResume")
    }

    // onPause se llama cuando el usuario está abandonando la actividad (primera señal).
    override fun onPause() {
        super.onPause()
        Log.d("CicloVida", "Ejecutando desde el método onPause")
    }

    // onStop se llama cuando la actividad ya no es visible.
    override fun onStop() {
        super.onStop()
        Log.d("CicloVida", "Ejecutando desde el método onStop")
    }

    // onDestroy se llama antes de que la actividad sea destruida.
    override fun onDestroy() {
        super.onDestroy()
        Log.d("CicloVida", "Ejecutando desde el método onDestroy")
    }

    // onRestart se llama cuando la actividad se reinicia después de haberse detenido.
    override fun onRestart() {
        super.onRestart()
        Log.d("CicloVida", "Ejecutando desde el método onRestart")
    }
}
