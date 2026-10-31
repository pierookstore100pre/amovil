package edu.iespvigil.sqlite01

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlite01.R

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
    }

    class miSQLiteHelper(context: Context) : SQLiteOpenHelper(context, "amigos.db", null,
        1){
        override fun onCreate(db: SQLiteDatabase?) {
            TODO("Not yet implemented")
        }

        override fun onUpgrade(
            db: SQLiteDatabase?,
            oldVersion: Int,
            newVersion: Int
        ) {
            TODO("Not yet implemented")
        }
    }


}
