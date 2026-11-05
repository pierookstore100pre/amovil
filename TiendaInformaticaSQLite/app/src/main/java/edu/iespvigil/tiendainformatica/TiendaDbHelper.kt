package edu.iespvigil.tiendainformatica

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Base de datos SQLite de la tienda de informática.
 *
 * Es la versión para Android del esquema MySQL del proyecto web "ti":
 * tablas categorias, marcas y productos. Sigue el mismo estilo de miSQLiteHelper
 * de la guía 03_02 (SQLiteOpenHelper + ContentValues + rawQuery).
 */
class TiendaDbHelper(context: Context) :
    SQLiteOpenHelper(context, "tienda_informatica.db", null, 1) {

    companion object {
        const val TABLA_CATEGORIAS = "categorias"
        const val TABLA_MARCAS = "marcas"

        /** Igual que STOCK_CRITICO del proyecto web: 5 unidades o menos = pocas unidades. */
        const val STOCK_CRITICO = 5

        // Consulta base de productos: une categoría y marca (la marca es opcional)
        private const val SELECT_PRODUCTO =
            "SELECT p.id, p.categoria_id, p.marca_id, p.nombre, p.descripcion, " +
                    "p.descripcion_larga, p.precio, p.stock, p.a_pedido, p.activo, " +
                    "p.updated_at, c.nombre, m.nombre " +
                    "FROM productos p " +
                    "JOIN categorias c ON c.id = p.categoria_id " +
                    "LEFT JOIN marcas m ON m.id = p.marca_id"
    }

    // Activa las claves foráneas (SQLite las trae desactivadas por defecto)
    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    // Crea las tablas la primera vez que se abre la base de datos y carga datos de ejemplo
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE categorias (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL, " +
                    "descripcion TEXT, " +
                    "activo INTEGER NOT NULL DEFAULT 1)"
        )
        db.execSQL(
            "CREATE TABLE marcas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL, " +
                    "activo INTEGER NOT NULL DEFAULT 1)"
        )
        db.execSQL(
            "CREATE TABLE productos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "categoria_id INTEGER NOT NULL REFERENCES categorias(id), " +
                    "marca_id INTEGER REFERENCES marcas(id), " +
                    "nombre TEXT NOT NULL, " +
                    "descripcion TEXT, " +
                    "descripcion_larga TEXT, " +
                    "precio REAL NOT NULL, " +
                    "stock INTEGER NOT NULL DEFAULT 0, " +
                    "a_pedido INTEGER NOT NULL DEFAULT 0, " +
                    "activo INTEGER NOT NULL DEFAULT 1, " +
                    "created_at TEXT NOT NULL DEFAULT (datetime('now','localtime')), " +
                    "updated_at TEXT NOT NULL DEFAULT (datetime('now','localtime')))"
        )
        cargarDatosDeEjemplo(db)
    }

    // Si cambia la versión, borra las tablas y las vuelve a crear (como en la guía)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS productos")
        db.execSQL("DROP TABLE IF EXISTS marcas")
        db.execSQL("DROP TABLE IF EXISTS categorias")
        onCreate(db)
    }

    // -----------------------------------------------------------------------
    // Datos de ejemplo: los mismos de 01_BD_tienda_informatica.sql y
    // 02_actualizacion_tienda_informatica.sql del proyecto web
    // -----------------------------------------------------------------------
    private fun cargarDatosDeEjemplo(db: SQLiteDatabase) {
        // Categorías (ids 1 a 4)
        sembrarCategoria(db, "Cargadores", "Cargadores de pared y cables")
        sembrarCategoria(db, "Almacenamiento", "Discos duros, SSD, memorias USB")
        sembrarCategoria(db, "Memorias RAM", "Memorias para laptops y PC")
        sembrarCategoria(db, "Cases y Adaptadores", "Cases para discos, adaptadores de red, etc.")

        // Marcas (ids 1 a 5)
        for (marca in listOf("Genérica", "Kingston", "Anker", "Seagate", "TP-Link")) {
            val v = ContentValues()
            v.put("nombre", marca)
            db.insert(TABLA_MARCAS, null, v)
        }

        // Productos: categoría, marca, nombre, descripción, descripción larga, precio, stock, a pedido
        sembrarProducto(
            db, 1, 3, "Cargador Rápido 20W", "Cargador de pared con puerto USB-C",
            "Entrada 100-240V AC, salida 5V/3A, 9V/2.2A. Carga rápida PD de 20W. Compatible con iPhone y Android.",
            45.0, 25, false
        )
        sembrarProducto(
            db, 2, 2, "Memoria USB 64GB", "Memoria USB 3.0",
            "USB 3.0, velocidad de lectura de hasta 100 MB/s. Compatible con Windows, macOS y Linux.",
            35.0, 3, false
        )
        sembrarProducto(
            db, 3, 2, "Memoria RAM 8GB DDR4", "Memoria RAM para laptop",
            "DDR4 SODIMM 3200 MHz, 1.2V. Para laptops. Se trae a pedido (consultar tiempo de entrega).",
            120.0, 0, true
        )
        sembrarProducto(
            db, 2, 4, "Disco Duro Externo 1TB", "Disco portátil USB 3.0",
            "USB 3.0, 5400 RPM, alimentación por USB, compatible con Windows y macOS.",
            230.0, 12, false
        )
        sembrarProducto(
            db, 4, 5, "Adaptador de Red USB TP-Link", "Adaptador WiFi USB 300Mbps",
            "Estándar 802.11n, hasta 300 Mbps, USB 2.0.",
            55.0, 8, false
        )
        sembrarProducto(
            db, 4, 1, "Case para Disco 2.5\"", "Case USB 3.0 para HDD/SSD SATA",
            "Soporta discos SATA de 2.5\", USB 3.0, sin herramientas.",
            40.0, 4, false
        )
        sembrarProducto(
            db, 1, 3, "Cable USB-C a USB-C 1m", "Cable de carga y datos",
            "Soporta 60W de carga, trenzado de nailon, 1 metro.",
            29.0, 0, false
        )
    }

    private fun sembrarCategoria(db: SQLiteDatabase, nombre: String, descripcion: String) {
        val v = ContentValues()
        v.put("nombre", nombre)
        v.put("descripcion", descripcion)
        db.insert(TABLA_CATEGORIAS, null, v)
    }

    private fun sembrarProducto(
        db: SQLiteDatabase, categoriaId: Int, marcaId: Int, nombre: String,
        descripcion: String, descripcionLarga: String, precio: Double,
        stock: Int, aPedido: Boolean
    ) {
        val v = ContentValues()
        v.put("categoria_id", categoriaId)
        v.put("marca_id", marcaId)
        v.put("nombre", nombre)
        v.put("descripcion", descripcion)
        v.put("descripcion_larga", descripcionLarga)
        v.put("precio", precio)
        v.put("stock", stock)
        v.put("a_pedido", if (aPedido) 1 else 0)
        db.insert("productos", null, v)
    }

    // -----------------------------------------------------------------------
    // PRODUCTOS
    // -----------------------------------------------------------------------

    /** Lista productos; se puede filtrar por texto (nombre o descripción), categoría y marca. */
    fun listarProductos(texto: String = "", categoriaId: Int = 0, marcaId: Int = 0): List<Producto> {
        val sql = StringBuilder(SELECT_PRODUCTO).append(" WHERE 1 = 1")
        val args = ArrayList<String>()

        if (texto.isNotBlank()) {
            sql.append(" AND (p.nombre LIKE ? OR p.descripcion LIKE ?)")
            args.add("%$texto%")
            args.add("%$texto%")
        }
        if (categoriaId > 0) {
            sql.append(" AND p.categoria_id = ?")
            args.add(categoriaId.toString())
        }
        if (marcaId > 0) {
            sql.append(" AND p.marca_id = ?")
            args.add(marcaId.toString())
        }
        sql.append(" ORDER BY p.nombre COLLATE NOCASE")

        val lista = ArrayList<Producto>()
        val cursor = readableDatabase.rawQuery(sql.toString(), args.toTypedArray())
        cursor.use {
            while (it.moveToNext()) {
                lista.add(leerProducto(it))
            }
        }
        return lista
    }

    /** Devuelve un producto por su id, o null si no existe. */
    fun obtenerProducto(id: Int): Producto? {
        val cursor = readableDatabase.rawQuery(
            "$SELECT_PRODUCTO WHERE p.id = ?", arrayOf(id.toString())
        )
        return cursor.use { if (it.moveToFirst()) leerProducto(it) else null }
    }

    /** Añade un producto. Devuelve el id nuevo (o -1 si falla). */
    fun anadirProducto(p: Producto): Long {
        return writableDatabase.insert("productos", null, valoresDe(p))
    }

    /** Modifica un producto según su id. Devuelve cuántos registros se modificaron. */
    fun modificarProducto(p: Producto): Int {
        val datos = valoresDe(p)
        datos.put("updated_at", fechaActual())
        return writableDatabase.update("productos", datos, "id = ?", arrayOf(p.id.toString()))
    }

    /** Borra un producto según su id y devuelve cuántos registros se borraron. */
    fun borrarProducto(id: Int): Int {
        return writableDatabase.delete("productos", "id = ?", arrayOf(id.toString()))
    }

    /**
     * Registra una entrada (suma) o una salida (resta) de stock.
     * Devuelve el stock nuevo, o -1 si el producto no existe o la salida supera el stock.
     */
    fun ajustarStock(id: Int, cantidad: Int, esEntrada: Boolean): Int {
        val actual = obtenerProducto(id)?.stock ?: return -1
        val nuevo = if (esEntrada) actual + cantidad else actual - cantidad
        if (nuevo < 0) return -1

        val datos = ContentValues()
        datos.put("stock", nuevo)
        datos.put("updated_at", fechaActual())
        writableDatabase.update("productos", datos, "id = ?", arrayOf(id.toString()))
        return nuevo
    }

    // -----------------------------------------------------------------------
    // CATEGORÍAS y MARCAS (mismo mantenimiento para las dos tablas)
    // -----------------------------------------------------------------------

    /** Lista (id, nombre) de la tabla "categorias" o "marcas". */
    fun listarMaestro(tabla: String): List<Opcion> {
        validarTabla(tabla)
        val lista = ArrayList<Opcion>()
        val cursor = readableDatabase.rawQuery(
            "SELECT id, nombre FROM $tabla ORDER BY nombre COLLATE NOCASE", null
        )
        cursor.use {
            while (it.moveToNext()) {
                lista.add(Opcion(it.getInt(0), it.getString(1)))
            }
        }
        return lista
    }

    /** Si id es 0 inserta un registro nuevo; si no, modifica el nombre del registro con ese id. */
    fun guardarMaestro(tabla: String, id: Int, nombre: String) {
        validarTabla(tabla)
        val datos = ContentValues()
        datos.put("nombre", nombre)
        if (id == 0) {
            writableDatabase.insert(tabla, null, datos)
        } else {
            writableDatabase.update(tabla, datos, "id = ?", arrayOf(id.toString()))
        }
    }

    /**
     * Borra una categoría o marca.
     * Devuelve 1 si se borró, 0 si no existía y -1 si no se puede borrar porque hay productos que la usan.
     */
    fun borrarMaestro(tabla: String, id: Int): Int {
        validarTabla(tabla)
        return try {
            writableDatabase.delete(tabla, "id = ?", arrayOf(id.toString()))
        } catch (e: SQLiteConstraintException) {
            -1
        }
    }

    // -----------------------------------------------------------------------
    // Funciones auxiliares
    // -----------------------------------------------------------------------

    private fun validarTabla(tabla: String) {
        require(tabla == TABLA_CATEGORIAS || tabla == TABLA_MARCAS) { "Tabla no permitida: $tabla" }
    }

    private fun valoresDe(p: Producto): ContentValues {
        val datos = ContentValues()
        datos.put("categoria_id", p.categoriaId)
        if (p.marcaId > 0) datos.put("marca_id", p.marcaId) else datos.putNull("marca_id")
        datos.put("nombre", p.nombre)
        datos.put("descripcion", p.descripcion)
        datos.put("descripcion_larga", p.descripcionLarga)
        datos.put("precio", p.precio)
        datos.put("stock", p.stock)
        datos.put("a_pedido", if (p.aPedido) 1 else 0)
        datos.put("activo", if (p.activo) 1 else 0)
        return datos
    }

    // Convierte la fila actual del cursor en un objeto Producto (el orden coincide con SELECT_PRODUCTO)
    private fun leerProducto(c: Cursor): Producto {
        return Producto(
            id = c.getInt(0),
            categoriaId = c.getInt(1),
            marcaId = if (c.isNull(2)) 0 else c.getInt(2),
            nombre = c.getString(3),
            descripcion = c.getString(4) ?: "",
            descripcionLarga = c.getString(5) ?: "",
            precio = c.getDouble(6),
            stock = c.getInt(7),
            aPedido = c.getInt(8) == 1,
            activo = c.getInt(9) == 1,
            actualizado = c.getString(10) ?: "",
            categoria = c.getString(11) ?: "",
            marca = c.getString(12) ?: ""
        )
    }

    private fun fechaActual(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
}
