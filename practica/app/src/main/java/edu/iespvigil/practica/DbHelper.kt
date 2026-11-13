package edu.iespvigil.practica

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Base de datos local SQLite llamada "bd_vigil"
class DbHelper(context: Context) : SQLiteOpenHelper(context, "bd_vigil", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla de estudiantes (campos comunes + usuario y contraseña)
        db.execSQL(
            """CREATE TABLE estudiante (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombres TEXT NOT NULL,
                apellidos TEXT NOT NULL,
                dni TEXT,
                correo TEXT,
                telefono TEXT,
                usuario TEXT NOT NULL UNIQUE,
                contrasena TEXT NOT NULL)"""
        )
        // Tabla donde se guardan los trámites
        db.execSQL(
            """CREATE TABLE tramite (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                requisitos TEXT NOT NULL,
                costo TEXT NOT NULL)"""
        )
        // Tabla de trámites iniciados por cada estudiante
        db.execSQL(
            """CREATE TABLE solicitud_tramite (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_estudiante INTEGER NOT NULL,
                id_tramite INTEGER NOT NULL,
                fecha TEXT NOT NULL,
                estado TEXT NOT NULL DEFAULT 'Pendiente',
                FOREIGN KEY (id_estudiante) REFERENCES estudiante(id),
                FOREIGN KEY (id_tramite) REFERENCES tramite(id))"""
        )
        cargarDatosIniciales(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS solicitud_tramite")
        db.execSQL("DROP TABLE IF EXISTS tramite")
        db.execSQL("DROP TABLE IF EXISTS estudiante")
        onCreate(db)
    }

    private fun cargarDatosIniciales(db: SQLiteDatabase) {
        val est = ContentValues().apply {
            put("nombres", "Juan Carlos")
            put("apellidos", "Pérez Quispe")
            put("dni", "70123456")
            put("correo", "juan.perez@correo.com")
            put("telefono", "952123456")
            put("usuario", "estudiante")
            put("contrasena", "1234")
        }
        db.insert("estudiante", null, est)

        val tramites = listOf(
            arrayOf("Constancia de estudios", "Documento que acredita que el estudiante está matriculado en el periodo actual.", "Solicitud simple, copia de DNI y voucher de pago.", "S/ 10.00"),
            arrayOf("Certificado de notas", "Documento oficial con las calificaciones obtenidas en cada semestre.", "Solicitud, copia de DNI, fotografía tamaño carné y voucher de pago.", "S/ 20.00"),
            arrayOf("Constancia de matrícula", "Documento que certifica la matrícula vigente en el programa de estudios.", "Solicitud simple y voucher de pago.", "S/ 10.00"),
            arrayOf("Reserva de matrícula", "Permite conservar la vacante del estudiante durante un periodo académico.", "Solicitud dirigida a dirección y voucher de pago.", "S/ 30.00"),
            arrayOf("Traslado interno", "Cambio de turno o de programa de estudios dentro de la institución.", "Solicitud, copia de DNI y constancia de notas.", "S/ 25.00"),
            arrayOf("Duplicado de carné", "Emisión de un nuevo carné de estudiante por pérdida o deterioro.", "Solicitud, fotografía tamaño carné y voucher de pago.", "S/ 15.00")
        )
        for (t in tramites) {
            val cv = ContentValues().apply {
                put("nombre", t[0]); put("descripcion", t[1]); put("requisitos", t[2]); put("costo", t[3])
            }
            db.insert("tramite", null, cv)
        }
    }

    // Valida usuario y contraseña guardados en la base de datos
    fun login(usuario: String, clave: String): Estudiante? {
        val c = readableDatabase.rawQuery(
            "SELECT id, nombres, apellidos FROM estudiante WHERE usuario = ? AND contrasena = ?",
            arrayOf(usuario, clave)
        )
        c.use {
            return if (it.moveToFirst()) Estudiante(it.getInt(0), it.getString(1) + " " + it.getString(2)) else null
        }
    }

    fun listarTramites(): List<Tramite> {
        val lista = ArrayList<Tramite>()
        val c = readableDatabase.rawQuery("SELECT id, nombre, descripcion, requisitos, costo FROM tramite ORDER BY id", null)
        c.use {
            while (it.moveToNext()) {
                lista.add(Tramite(it.getInt(0), it.getString(1), it.getString(2), it.getString(3), it.getString(4)))
            }
        }
        return lista
    }

    fun obtenerTramite(id: Int): Tramite? {
        val c = readableDatabase.rawQuery(
            "SELECT id, nombre, descripcion, requisitos, costo FROM tramite WHERE id = ?",
            arrayOf(id.toString())
        )
        c.use {
            return if (it.moveToFirst()) Tramite(it.getInt(0), it.getString(1), it.getString(2), it.getString(3), it.getString(4)) else null
        }
    }

    // Botón "Iniciar trámite": guarda el trámite como pendiente con la fecha actual
    fun iniciarTramite(idEstudiante: Int, idTramite: Int): Boolean {
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val cv = ContentValues().apply {
            put("id_estudiante", idEstudiante)
            put("id_tramite", idTramite)
            put("fecha", fecha)
            put("estado", "Pendiente")
        }
        return writableDatabase.insert("solicitud_tramite", null, cv) != -1L
    }

    fun listarPendientes(idEstudiante: Int): List<Pendiente> {
        val lista = ArrayList<Pendiente>()
        val c = readableDatabase.rawQuery(
            """SELECT s.id, t.nombre, s.fecha, e.nombres || ' ' || e.apellidos, s.estado
               FROM solicitud_tramite s
               JOIN tramite t ON t.id = s.id_tramite
               JOIN estudiante e ON e.id = s.id_estudiante
               WHERE s.id_estudiante = ? AND s.estado = 'Pendiente'
               ORDER BY s.id DESC""",
            arrayOf(idEstudiante.toString())
        )
        c.use {
            while (it.moveToNext()) {
                lista.add(Pendiente(it.getInt(0), it.getString(1), it.getString(2), it.getString(3), it.getString(4)))
            }
        }
        return lista
    }
}
