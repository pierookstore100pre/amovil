package edu.iespvigil.a03_02_guia_sqlite02

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class miSQLiteHelper(context: Context) : SQLiteOpenHelper(context, "amigos.db", null, 1) {

    // Crea la tabla "amigos" la primera vez que se abre la base de datos
    override fun onCreate(p0: SQLiteDatabase?) {
        val ordenCreacion = "CREATE TABLE amigos" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, email TEXT) "
        p0!!.execSQL(ordenCreacion)
    }

    // Si cambia la versión, borra la tabla y la vuelve a crear
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val ordenBorrado = "DROP TABLE IF EXISTS amigos"
        p0!!.execSQL(ordenBorrado)
        onCreate(p0)
    }

    // Añade un registro a la tabla "amigos"
    fun anadirDato(nombre: String, email: String) {
        val datos = ContentValues()
        datos.put("nombre", nombre)
        datos.put("email", email)

        val db = this.writableDatabase
        db.insert("amigos", null, datos)
        db.close()
    }

    // Modifica el nombre y el email de un registro según su id
    fun modificarDato(id: Int, nombre: String, email: String) {
        val args = arrayOf(id.toString())

        val datos = ContentValues()
        datos.put("nombre", nombre)
        datos.put("email", email)

        val db = this.writableDatabase
        db.update("amigos", datos, "_id = ?", args)
        db.close()
    }

    // Borra un registro según su id y devuelve cuántos registros se borraron
    fun borrarDato(id: Int): Int {
        val args = arrayOf(id.toString())

        val db = this.writableDatabase
        val borrados = db.delete("amigos", "_id = ?", args)
        db.close()
        return borrados
    }
}
