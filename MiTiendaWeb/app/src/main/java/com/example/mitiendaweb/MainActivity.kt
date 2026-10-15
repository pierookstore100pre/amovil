package com.example.mitiendaweb

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    // 1. Declaramos las variables para el WebView y la barra de progreso
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // <-- AÑADE ESTA LÍNEA
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2. Capturamos los objetos del layout
        webView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.progress)

        // 3. Habilitamos servicios para que el WebView funcione bien
        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
        }

        // 4. Configuramos el cliente que carga la web
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url ?: return false
                val scheme = url.scheme ?: return false
                return if (scheme == "http" || scheme == "https") {
                    // Deja que el WebView lo cargue
                    false
                } else {
                    // Abre otros esquemas (tel:, mailto:, etc.) con apps externas
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, url))
                    } catch (_: ActivityNotFoundException) {
                        Toast.makeText(this@MainActivity, "No hay app para abrir el enlace.", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
            }
        }

        // 5. Configuramos la barra de progreso
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.visibility = if (newProgress < 100) View.VISIBLE else View.GONE
                progressBar.progress = newProgress
            }
            override fun onReceivedTitle(view: WebView?, title: String?) {
                supportActionBar?.title = title
            }
        }

        // 6. Cargamos nuestro sitio web
        webView.loadUrl("https://apstivigil.edu.pe/2026/disenoweb/nocturno/202644/")

        // 7. Configuramos el botón "Atrás"
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    // 8. Métodos para guardar y restaurar el estado (para que no se recargue al girar la pantalla)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }
}