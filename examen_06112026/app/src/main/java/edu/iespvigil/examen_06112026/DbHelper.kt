package edu.iespvigil.examen_06112026

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Estudiante(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val dni: String,
    val correo: String,
    val telefono: String,
    val usuario: String
) {
    val nombreCompleto: String get() = "$nombres $apellidos"
}

data class Tramite(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val requisitos: String,
    val costo: Double
)

data class TramitePendiente(
    val id: Int,
    val tramite: String,
    val fechaInicio: String,
    val estudiante: String,
    val estado: String
)

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "bd_vigil"
        const val DB_VERSION = 1
        const val ESTADO_PENDIENTE = "PENDIENTE"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla de estudiantes (incluye usuario y contraseña)
        db.execSQL(
            """
            CREATE TABLE estudiante (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombres TEXT NOT NULL,
                apellidos TEXT NOT NULL,
                dni TEXT NOT NULL,
                correo TEXT,
                telefono TEXT,
                usuario TEXT NOT NULL UNIQUE,
                contrasena TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Tabla de trámites (catálogo)
        db.execSQL(
            """
            CREATE TABLE tramite (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                requisitos TEXT NOT NULL,
                costo REAL NOT NULL
            )
            """.trimIndent()
        )

        // Tabla de trámites iniciados: quién lo inició, cuál trámite y la fecha
        db.execSQL(
            """
            CREATE TABLE tramite_estudiante (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_estudiante INTEGER NOT NULL,
                id_tramite INTEGER NOT NULL,
                fecha_inicio TEXT NOT NULL,
                estado TEXT NOT NULL DEFAULT 'PENDIENTE',
                FOREIGN KEY (id_estudiante) REFERENCES estudiante(id),
                FOREIGN KEY (id_tramite) REFERENCES tramite(id)
            )
            """.trimIndent()
        )

        sembrarDatos(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tramite_estudiante")
        db.execSQL("DROP TABLE IF EXISTS tramite")
        db.execSQL("DROP TABLE IF EXISTS estudiante")
        onCreate(db)
    }

    private fun sembrarDatos(db: SQLiteDatabase) {
        db.insert("estudiante", null, ContentValues().apply {
            put("nombres", "Abel")
            put("apellidos", "Vigil")
            put("dni", "12345678")
            put("correo", "alex@vigil.edu.pe")
            put("telefono", "999888777")
            put("usuario", "Alex")
            put("contrasena", "abel12345")
        })

        val tramites = listOf(
            arrayOf(
                "Solicitar carta de presentación",
                "Documento oficial de la institución que presenta al estudiante ante una empresa para realizar prácticas.",
                "• Copia de DNI\n• Ficha de matrícula vigente\n• Datos de la empresa",
                "15.00"
            ),
            arrayOf(
                "Bachiller",
                "Trámite para obtener el grado académico de bachiller.",
                "• Copia de DNI\n• Certificado de estudios\n• Constancia de no adeudo\n• 4 fotos tamaño pasaporte",
                "350.00"
            ),
            arrayOf(
                "Constancia de estudios",
                "Documento que acredita que el estudiante está matriculado actualmente.",
                "• Copia de DNI\n• Recibo de pago",
                "10.00"
            ),
            arrayOf(
                "Certificado de notas",
                "Documento con el historial de calificaciones por semestre.",
                "• Copia de DNI\n• Recibo de pago\n• Foto tamaño carné",
                "25.00"
            ),
            arrayOf(
                "Constancia de egresado",
                "Documento que acredita la culminación del plan de estudios.",
                "• Copia de DNI\n• Certificado de notas\n• Constancia de no adeudo",
                "40.00"
            ),
            arrayOf(
                "Duplicado de carné",
                "Solicitud de un nuevo carné universitario por pérdida o deterioro.",
                "• Copia de DNI\n• Denuncia policial (en caso de pérdida)\n• Foto tamaño carné",
                "12.00"
            )
        )
        for (t in tramites) {
            db.insert("tramite", null, ContentValues().apply {
                put("nombre", t[0])
                put("descripcion", t[1])
                put("requisitos", t[2])
                put("costo", t[3].toDouble())
            })
        }
    }

    // ---------------- Estudiante ----------------

    /** Devuelve el estudiante si usuario y contraseña coinciden, o null. */
    fun login(usuario: String, contrasena: String): Estudiante? {
        readableDatabase.rawQuery(
            "SELECT id, nombres, apellidos, dni, correo, telefono, usuario " +
                "FROM estudiante WHERE usuario = ? AND contrasena = ?",
            arrayOf(usuario, contrasena)
        ).use { c ->
            if (c.moveToFirst()) {
                return Estudiante(
                    c.getInt(0), c.getString(1), c.getString(2), c.getString(3),
                    c.getString(4) ?: "", c.getString(5) ?: "", c.getString(6)
                )
            }
        }
        return null
    }

    // ---------------- Trámites ----------------

    fun listarTramites(): List<Tramite> {
        val lista = mutableListOf<Tramite>()
        readableDatabase.rawQuery(
            "SELECT id, nombre, descripcion, requisitos, costo FROM tramite ORDER BY nombre",
            null
        ).use { c ->
            while (c.moveToNext()) {
                lista.add(Tramite(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getDouble(4)))
            }
        }
        return lista
    }

    fun obtenerTramite(id: Int): Tramite? {
        readableDatabase.rawQuery(
            "SELECT id, nombre, descripcion, requisitos, costo FROM tramite WHERE id = ?",
            arrayOf(id.toString())
        ).use { c ->
            if (c.moveToFirst()) {
                return Tramite(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getDouble(4))
            }
        }
        return null
    }

    // ---------------- Trámites iniciados ----------------

    fun existePendiente(idEstudiante: Int, idTramite: Int): Boolean {
        readableDatabase.rawQuery(
            "SELECT 1 FROM tramite_estudiante WHERE id_estudiante = ? AND id_tramite = ? AND estado = ?",
            arrayOf(idEstudiante.toString(), idTramite.toString(), ESTADO_PENDIENTE)
        ).use { c -> return c.moveToFirst() }
    }

    /** Registra quién inició el trámite y la fecha de inicio. Devuelve el id insertado. */
    fun iniciarTramite(idEstudiante: Int, idTramite: Int): Long {
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        return writableDatabase.insert("tramite_estudiante", null, ContentValues().apply {
            put("id_estudiante", idEstudiante)
            put("id_tramite", idTramite)
            put("fecha_inicio", fecha)
            put("estado", ESTADO_PENDIENTE)
        })
    }

    fun listarPendientes(): List<TramitePendiente> {
        val lista = mutableListOf<TramitePendiente>()
        readableDatabase.rawQuery(
            """
            SELECT te.id, t.nombre, te.fecha_inicio, e.nombres || ' ' || e.apellidos, te.estado
            FROM tramite_estudiante te
            INNER JOIN tramite t ON t.id = te.id_tramite
            INNER JOIN estudiante e ON e.id = te.id_estudiante
            WHERE te.estado = ?
            ORDER BY te.id DESC
            """.trimIndent(),
            arrayOf(ESTADO_PENDIENTE)
        ).use { c ->
            while (c.moveToNext()) {
                lista.add(TramitePendiente(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)))
            }
        }
        return lista
    }
}
