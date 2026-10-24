package edu.iespvigil.menulateral01

import android.os.Bundle
import androidx.activity.enableEdgeToEdge

class Item7Activity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item1)

        setupDrawer()
        supportActionBar?.title = "Perfil"
    }
}
