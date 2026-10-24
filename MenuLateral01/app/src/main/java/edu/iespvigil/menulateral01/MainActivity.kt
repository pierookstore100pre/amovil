package edu.iespvigil.menulateral01

import android.os.Bundle
import androidx.activity.enableEdgeToEdge

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupDrawer()
        supportActionBar?.title = getString(R.string.app_name)
    }
}
